package software.bigbade.javaskript.compiler.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.LocalVariablesSorter;
import software.bigbade.javaskript.api.exception.IllegalScriptException;
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
import software.bigbade.javaskript.compiler.instructions.DropStackInstruction;
import software.bigbade.javaskript.compiler.instructions.InstanceofCall;
import software.bigbade.javaskript.compiler.instructions.MathCall;
import software.bigbade.javaskript.compiler.instructions.MethodCall;
import software.bigbade.javaskript.compiler.instructions.ReturnCall;
import software.bigbade.javaskript.compiler.instructions.SetArrayVariable;
import software.bigbade.javaskript.compiler.instructions.SetVariableCall;
import software.bigbade.javaskript.compiler.instructions.StaticMethodCall;
import software.bigbade.javaskript.compiler.java.BasicJavaCodeBlock;
import software.bigbade.javaskript.compiler.java.JavaCodeBlock;
import software.bigbade.javaskript.compiler.statements.IfStatement;
import software.bigbade.javaskript.compiler.statements.IfStatementType;
import software.bigbade.javaskript.compiler.variables.Loadable;
import software.bigbade.javaskript.compiler.variables.StackVariable;
import software.bigbade.javaskript.compiler.variables.StoredVariable;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class SkriptMethodBuilder<T> implements MethodLineConverter<T> {
    private static final String REQUIRED_METHOD_NAME = "runMethod";

    @Getter
    private final String name;
    @Nullable
    private final SkriptType<?> returnType;

    private final List<JavaCodeBlock> codeBlocks = new ArrayList<>();
    private final JavaClassWriter parent;

    private final Map<String, LocalVariable<?>> methodVariables = new HashMap<>();
    private final Deque<StackVariable<?>> stack = new ArrayDeque<>();
    private final Variables variables;

    private JavaCodeBlock currentBlock = null;

    public SkriptMethodBuilder(JavaClassWriter parent, String name, @Nullable SkriptType<?> returnType, Variables variables) {
        this.name = name;
        this.returnType = returnType;
        this.parent = parent;
        this.variables = variables;
        int i = 0;
        if(variables.getAllVariables().containsKey("this")) {
            registerVariable("this", variables.getAllVariables().get("this").getType()).setNumber(0);
            i = 1;
        }
        for (Map.Entry<String, SkriptType<?>> param : variables.getAllVariables().entrySet()) {
            if (param.getKey().equals("this")) {
                continue;
            }
            registerVariable(param.getKey(), param.getValue().getType()).setNumber(i++);
        }
    }

    @SneakyThrows
    private static void copyClass(Method method) {
        new ClassReader(method.getDeclaringClass().getName()).accept(new ClassVisitor(Opcodes.ASM9) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                if(method.getName().equals(name)) {
                    return new MethodRemapper(method);
                }
                return super.visitMethod(access, name, descriptor, signature, exceptions);
            }
        }, ClassReader.SKIP_FRAMES);
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
        for (Map.Entry<String, SkriptType<?>> entry : variables.getAllVariables().entrySet()) {
            if (!entry.getKey().equals("this")) {
                entry.getValue().getType().getDescriptor(builder);
            }
        }
        builder.append(")");
        if (returnType == null) {
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
        codeBlocks.add(block);
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
    public <E> MethodLineConverter<E> callSkriptMethod(ParsedSkriptMethod method) {
        if (method.getReturnType() != null) {
            return setStack(new StackVariable<>(method.getReturnType().getType(), (builder, code) -> {
                copyClass(getRunMethod(method.getMethod().getClass()).orElseThrow(() ->
                        new IllegalStateException("Class " + method.getMethod().getClass() + " has no runMethod method!")));
                new MethodCall<>("Utils", REQUIRED_METHOD_NAME, null,
                        method.getMethod().getVariables()).addInstructions(builder, code);
            }));
        } else {
            addInstruction(((builder, code) -> {
                copyClass(getRunMethod(method.getMethod().getClass()).orElseThrow(() ->
                        new IllegalStateException("Class " + method.getMethod().getClass() + " has no runMethod method!")));
                addInstruction(new MethodCall<>("Utils", REQUIRED_METHOD_NAME, null,
                        method.getMethod().getVariables()));
            }));
        }
        return setStack(null);
    }

    private static Optional<Method> getRunMethod(Class<?> clazz) {
        for(Method method : clazz.getMethods()) {
            if(method.getName().equals(REQUIRED_METHOD_NAME)) {
                return Optional.of(method);
            }
        }
        return Optional.empty();
    }

    @Override
    public <E> MethodLineConverter<E> newInstance(String clazz, SkriptType<?>... args) {
        Type type = Type.getType(clazz);
        return setStack(new StackVariable<>(type, new MethodCall<>(clazz, "<init>", type, args)::addInstructions));
    }

    @Override
    public <E> MethodLineConverter<E> callJavaMethod(String clazz, String method, @Nullable SkriptType<E> returnType, boolean staticMethod, SkriptType<?>... args) {
        BasicInstruction instruction;
        if (staticMethod) {
            instruction = new StaticMethodCall<>(clazz, method, returnType == null ? null : returnType.getType(), args);
        } else {
            instruction = new MethodCall<>(clazz, method, returnType == null ? null : returnType.getType(), args);
        }
        if(returnType == null) {
            addInstruction(instruction);
            return setStack(null);
        } else {
            return setStack(new StackVariable<>(returnType.getType(), instruction::addInstructions));
        }
    }

    @SuppressWarnings("unchecked")
    private <E> MethodLineConverter<E> setStack(@Nullable StackVariable<E> variable) {
        if (variable != null) {
            stack.push(variable);
        }
        return (MethodLineConverter<E>) this;
    }

    @Override
    public <E> MethodLineConverter<E> manipulateVariable(VariableChanges change, SkriptType<E> first, @Nullable SkriptType<?> second) {
        MathCall<E> call = new MathCall<>(change, first, second);
        return setStack(call.getOutput().orElseThrow(EmptyStackException::new));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E> MethodLineConverter<E> convertVariable(SkriptType<?> type, Type convertTo) {
        ConvertVariableCall<?> convert = new ConvertVariableCall<>(type, convertTo);
        return (MethodLineConverter<E>) setStack(convert.getOutput().orElseThrow(IllegalStateException::new));
    }

    @Override
    public MethodLineConverter<Boolean> isInstanceOf(Type type) {
        StackVariable<Boolean> output = new InstanceofCall(type).getOutput().orElseThrow(IllegalStateException::new);
        return setStack(output);
    }

    @Override
    public <C> void setVariable(LocalVariable<C> variable) {
        addInstruction(new SetVariableCall(variable));
    }

    @Override
    public <C> void setArrayVariable(LocalVariable<C> variable, int index) {
        addInstruction(new SetArrayVariable(variable, index));
    }

    @Override
    public <E> MethodLineConverter<E> dropTopStack() {
        addInstruction(new DropStackInstruction());
        return setStack(null);
    }

    @Override
    public void returnVariable() {
        addInstruction(new ReturnCall(false));
    }

    @Override
    public void returnNothing() {
        addInstruction(new ReturnCall(true));
    }

    @Override
    public MethodLineConverter<T> addJavaBlock(Statements statements, Object... args) {
        switch (statements) {
            case CODE_BLOCK:
                addJavaBlock(new BasicJavaCodeBlock());
                break;
            case IF_STATEMENTS:
                addJavaBlock(new IfStatement((IfStatementType) args[0]));
                break;
            default:
                throw new IllegalArgumentException("Statement " + statements.name() + " not implemented yet!");
        }
        return this;
    }

    @Override
    public void endJavaBlock() {
        currentBlock = currentBlock.getParent();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C> LocalVariable<C> getLocalVariable(String name) {
        return (LocalVariable<C>) methodVariables.get(name);
    }

    @Override
    public <E> MethodLineConverter<E> loadVariable(LocalVariable<E> variable) {
        if (variable == null) {
            throw new IllegalScriptException("Unknown variable!");
        }
        return setStack(new StackVariable<>(variable.getType(), ((Loadable) variable)::loadVariable));
    }

    @Override
    public <E> MethodLineConverter<E> loadArrayVariable(LocalVariable<E[]> variable, int index) {
        return setStack(new StackVariable<>(variable.getType(), ((Loadable) variable)::loadVariable));
    }

    @Override
    public <E> MethodLineConverter<E> loadConstant(E constant) {
        return setStack(new StackVariable<>(Type.getType(constant.getClass()), (builder, code) -> code.visitLdcInsn(constant)));
    }

    public void compose(MethodVisitor visitor) {
        LocalVariablesSorter sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, getMethodDescription(), visitor);
        int i = 0;
        for (LocalVariable<?> variable : methodVariables.values()) {
            if (i++ < variables.getAllVariables().size()) {
                continue;
            }
            variable.setNumber(sorter.newLocal(org.objectweb.asm.Type.getType(variable.getType().getDescriptor())));
        }
        for (JavaCodeBlock block : codeBlocks) {
            block.loadInstructions(this, visitor);
        }
        visitor.visitMaxs(0, 0);
    }
}
