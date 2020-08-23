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
import software.bigbade.javaskript.compiler.variables.StackVariable;
import software.bigbade.javaskript.compiler.variables.StoredVariable;

import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.EmptyStackException;
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

    private final Deque<StackVariable<?>> stack = new ArrayDeque<>();

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
    @Override
    public StackVariable<T> popStack() {
        //Needed so stack can be swapped along with the generic type
        return (StackVariable<T>) stack.pop();
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
    @SuppressWarnings("unchecked")
    @Override
    public <E> MethodLineConverter<E> callMethod(ParsedSkriptMethod method) {
        BasicInstruction instruction;
        if (method.getMethod().isConstructor()) {
            instruction = new CreateObjectCall<>(method.getMethod().getOwner(), method.getVariables().toArray(new SkriptType[0]));
        } else {
            instruction = new MethodCall<>(method.getMethod().getOwner(), method.getMethod().getName(), method.getReturnType() == null ? null : method.getReturnType().getType(), method.getVariables().toArray(new SkriptType[0]));
        }
        if(method.getReturnType() != null) {
            return setStack(new StackVariable<>(method.getReturnType().getType(), instruction::addInstructions));
        }
        addInstruction(instruction);
        return (MethodLineConverter<E>) this;
    }

    @SuppressWarnings("unchecked")
    private <E> MethodLineConverter<E> setStack(StackVariable<E> variable) {
        stack.push(variable);
        return (MethodLineConverter<E>) this;
    }

    @Override
    public <E> MethodLineConverter<E> manipulateVariable(VariableChanges change, SkriptType first, @Nullable SkriptType second) {
        MathCall<E> call = new MathCall<>(change, first, second);
        return setStack(call.getOutput().orElseThrow(EmptyStackException::new));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E> MethodLineConverter<E> convertVariable(SkriptType type, Type convertTo) {
        ConvertVariableCall<?> convert = new ConvertVariableCall<>(type, convertTo);
        return (MethodLineConverter<E>) setStack(convert.getOutput().orElseThrow(IllegalStateException::new));
    }

    @Override
    public <C> void setVariable(LocalVariable<C> variable, C value) {
        addInstruction(new PushVariableCall(value));
        addInstruction(new SetVariableCall(variable));
    }

    @Override
    public void returnVariable(SkriptType type) {
        addInstruction(new ReturnCall(type));
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
