package software.bigbade.javaskript.api.objects;

import software.bigbade.javaskript.api.instructions.Statements;
import software.bigbade.javaskript.api.instructions.VariableChanges;
import software.bigbade.javaskript.api.objects.variable.LocalVariable;
import software.bigbade.javaskript.api.variables.SkriptType;
import software.bigbade.javaskript.api.variables.Type;

import javax.annotation.Nullable;

public interface MethodLineConverter<T> {
    String getName();

    String getClassName();

    String getMethodDescription();

    <E> MethodLineConverter<E> callSkriptMethod(ParsedSkriptMethod method);

    <E> MethodLineConverter<E> callJavaMethod(String clazz, String method, @Nullable SkriptType<E> returnType, boolean staticMethod, SkriptType<?>... args);

    <E> MethodLineConverter<E> newInstance(String clazz, SkriptType<?>... args);

    <E> MethodLineConverter<E> manipulateVariable(VariableChanges change, SkriptType<E> first, @Nullable SkriptType<?> second);

    <E> MethodLineConverter<E> convertVariable(SkriptType<?> type, Type convertTo);

    MethodLineConverter<Boolean> isInstanceOf(Type type);

    <C> void setVariable(LocalVariable<C> variable);

    <C> void setArrayVariable(LocalVariable<C> variable, int index);

    <E> MethodLineConverter<E> dropTopStack();

    void returnVariable();

    void returnNothing();

    MethodLineConverter<T> addJavaBlock(Statements statements, Object... args);

    <C> LocalVariable<C> registerVariable(String name, Type type);

    <C> LocalVariable<C> getLocalVariable(String name);

    <E> MethodLineConverter<E> loadVariable(LocalVariable<E> variable);

    <E> MethodLineConverter<E> loadArrayVariable(LocalVariable<E[]> variable, int index);

    <E> MethodLineConverter<E> loadConstant(E constant);

    void endJavaBlock();

    LocalVariable<T> popStack();
}
