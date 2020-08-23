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

    <E> MethodLineConverter<E> callMethod(ParsedSkriptMethod method);

    <E> MethodLineConverter<E> manipulateVariable(VariableChanges change, SkriptType first, @Nullable SkriptType second);

    <E> MethodLineConverter<E> convertVariable(SkriptType type, Type convertTo);

    <C> void setVariable(LocalVariable<C> variable, C value);

    void returnVariable(SkriptType type);

    void returnNothing();

    MethodLineConverter<T> addJavaBlock(Statements statements, Object... args);

    <C> LocalVariable<C> registerVariable(String name, Type type);

    <C> LocalVariable<C> getLocalVariable(String name);

    <E> MethodLineConverter<E> loadVariable(LocalVariable<E> variable);

    <E> MethodLineConverter<E> loadConstant(E constant);

    LocalVariable<T> popStack();
}
