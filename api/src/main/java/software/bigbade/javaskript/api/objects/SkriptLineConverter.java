package software.bigbade.javaskript.api.objects;

import software.bigbade.javaskript.api.objects.variable.LocalVariable;
import software.bigbade.javaskript.api.variables.SkriptType;
import software.bigbade.javaskript.api.variables.Variables;

import javax.annotation.Nullable;

public interface SkriptLineConverter extends WriteableJavaClass {

    void endClass();

    MethodLineConverter<Void> startMethod(String name, Variables variables, @Nullable SkriptType returnType);

    void endMethod(MethodLineConverter<?> converter);

    void registerVariable(String name, SkriptType variable);

    <C> LocalVariable<C> getLocalVariable(String name);
}
