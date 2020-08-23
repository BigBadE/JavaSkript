package software.bigbade.javaskript.compiler.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.LocalVariablesSorter;
import software.bigbade.javaskript.api.instructions.Statements;
import software.bigbade.javaskript.api.instructions.VariableChanges;
import software.bigbade.javaskript.api.objects.MethodLineConverter;
import software.bigbade.javaskript.api.objects.ParsedSkriptMethod;
import software.bigbade.javaskript.api.objects.variable.LocalVariable;
import software.bigbade.javaskript.api.variables.SkriptType;
import software.bigbade.javaskript.api.variables.Type;
import software.bigbade.javaskript.api.variables.Variables;
import software.bigbade.javaskript.compiler.instructions.BasicInstruction;
import software.bigbade.javaskript.compiler.instructions.ConvertVariableCall;
import software.bigbade.javaskript.compiler.instructions.CreateObjectCall;
import software.bigbade.javaskript.compiler.instructions.LoadVariableCall;
import software.bigbade.javaskript.compiler.instructions.MathCall;
import software.bigbade.javaskript.compiler.instructions.MethodCall;
import software.bigbade.javaskript.compiler.instructions.PushVariableCall;
import software.bigbade.javaskript.compiler.instructions.ReturnCall;
import software.bigbade.javaskript.compiler.instructions.SetVariableCall;
import software.bigbade.javaskript.compiler.java.BasicJavaCodeBlock;
import software.bigbade.javaskript.compiler.java.JavaCodeBlock;
import software.bigbade.javaskript.compiler.statements.IfStatement;
import software.bigbade.javaskript.compiler.statements.IfStatementType;
import software.bigbade.javaskript.compiler.variables.ConstantVariable;
import software.bigbade.javaskript.compiler.variables.StackVariable;
import software.bigbade.javaskript.compiler.variables.StoredVariable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@RequiredArgsConstructor
public class SkriptMethodBuilder<T> implements MethodLineConverter<T> {
    @Getter
    private final String name;
    @Nullable
    private final SkriptType returnType;

    private final JavaClassWriter parent;

    private final Map<String, LocalVariable<?>> methodVariables = new HashMap<>();
    private final List<JavaCodeBlock> codeBlocks = new ArrayList<>();
    private final Variables variables;

    private JavaCodeBlock currentBlock = null;

    private StackVariable<?> stack = null;

    public SkriptMethodBuilder(JavaClassWriter parent, String name, @Nullable SkriptType returnType, Variables variables) {
        this.name = name;
        this.returnType = returnType;
        this.parent = parent;
        this.variables = variables;
        registerVariable("this", variables.getAllVariables().get("this").getType()).setNumber(0);
        int i = 1;
        for(Map.Entry<String, SkriptType> param : variables.getAllVariables().entrySet()) {
            if(param.getKey().equals("this")) {
                continue;
            }
            registerVariable(param.getKey(), param.getValue().getType()).setNumber(i++);
        }
    }

    public String getClassName() {
        return parent.getName();
    }

    @SuppressWarnings("unchecked")
    public StackVariable<T> getStack() {
        //Needed so stack can be swapped along with the generic type
        return (StackVariable<T>) stack;
    }
    public String getMethodDescription() {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        for (Map.Entry<String, SkriptType> entry : variables.getAllVariables().entrySet()) {
            if(!entry.getKey().equals("this")) {
                entry.getValue().getType().getDescriptor(builder);
            }
        }
        builder.append(")");
        if(returnType == null) {
            builder.append("V");
        } else {
            returnType.getType().getDescriptor(builder);
        }
        return builder.toString();
    }

    public void addInstruction(BasicInstruction instruction) {
        currentBlock.addInstruction(instruction);
    }

    public void addJavaBlock(JavaCodeBlock block) {
        if(currentBlock != null) {
            codeBlocks.add(currentBlock);
        }
        block.setParent(currentBlock);
        currentBlock = block;
    }

    @Override
    public <C> LocalVariable<C> registerVariable(String name, Type type) {
        LocalVariable<C> variable = new StoredVariable<>(type);
        methodVariables.put(name, variable);
        return variable;
    }

    /**
     * Calls a SkriptMethod
     *
     * @param method Method to call
     */
    @Override
    public <E> MethodLineConverter<E> callMethod(ParsedSkriptMethod method) {
        BiConsumer<MethodLineConverter<?>, MethodVisitor> consumer;
        if (method.getMethod().isConstructor()) {
            consumer = new CreateObjectCall<>(method.getMethod().getOwner(), method.getVariables().toArray(new LocalVariable[0]))::addInstructions;
        } else {
            consumer = new MethodCall<>(method.getMethod().getOwner(), method.getMethod().getName(), method.getReturnType() == null ? null : method.getReturnType().getType(), method.getVariables().toArray(new LocalVariable[0]))::addInstructions;
        }
        Type type = null;
        if(method.getReturnType() != null) {
            type = method.getReturnType().getType();
        }
        return setStack(new StackVariable<>(type, consumer));
    }

    @SuppressWarnings("unchecked")
    private <E> MethodLineConverter<E> setStack(StackVariable<E> variable) {
        this.stack = variable;
        return (MethodLineConverter<E>) this;
    }

    @Override
    public <E> MethodLineConverter<E> manipulateVariable(VariableChanges change, LocalVariable<E> first, LocalVariable<?> second) {
        MathCall<E> call = new MathCall<>(change, first, second);
        assert call.getOutput().isPresent();
        return setStack(call.getOutput().get());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E> MethodLineConverter<E> convertVariable(LocalVariable<?> variable, Type type) {
        ConvertVariableCall<?> convert = new ConvertVariableCall<>(variable, type);
        assert convert.getOutput().isPresent();
        return (MethodLineConverter<E>) setStack(convert.getOutput().get());
    }

    @Override
    public <C> LocalVariable<C> createConstant(C constant) {
        return new ConstantVariable<>(constant);
    }

    @Override
    public <C> void setVariable(LocalVariable<C> variable, C value) {
        addInstruction(new PushVariableCall(value));
        addInstruction(new SetVariableCall(variable));
    }

    @Override
    public <C> void returnVariable(LocalVariable<C> variable) {
        addInstruction(new ReturnCall(variable));
    }

    @Override
    public void returnNothing() {
        addInstruction(new ReturnCall(null));
    }

    @Override
    public MethodLineConverter<T> addJavaBlock(Statements statements, Object... args) {
        switch (statements) {
            case CODE_BLOCK:
                addJavaBlock(new BasicJavaCodeBlock());
                break;
            case IF_STATEMENTS:
                addJavaBlock(new IfStatement((IfStatementType) args[0], (LocalVariable<?>[]) args[1]));
                break;
            default:
                throw new IllegalArgumentException("Statement " + statements.name() + " not implemented yet!");
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C> LocalVariable<C> getLocalVariable(String name) {
        return (LocalVariable<C>) methodVariables.get(name);
    }

    @Override
    public <E> MethodLineConverter<E> loadVariable(LocalVariable<E> variable) {
        return setStack(new StackVariable<>(variable.getType(), new LoadVariableCall(variable)::addInstructions));
    }

    @Override
    public <E> MethodLineConverter<E> loadConstant(E constant) {
        return setStack(new StackVariable<>(Type.getType(constant.getClass()), (builder, code) -> code.visitLdcInsn(constant)));
    }

    public void compose(MethodVisitor visitor) {
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, getMethodDescription(), visitor);
        int i = 0;
        for(LocalVariable<?> variable : methodVariables.values()) {
            if(i++ < variables.getAllVariables().size()) {
                continue;
            }
            variable.setNumber(sorter.newLocal(org.objectweb.asm.Type.getType(variable.getType().getDescriptor())));
        }
        for(JavaCodeBlock block : codeBlocks) {
            block.loadInstructions(this, visitor);
        }
        currentBlock.loadInstructions(this, visitor);
        visitor.visitMaxs(0, 0);
    }
}
