package software.bigbade.javaskript.api;

import software.bigbade.javaskript.api.variables.Variables;
import software.bigbade.javaskript.api.objects.SkriptMethod;
import software.bigbade.javaskript.api.variables.SkriptType;

import javax.annotation.Nullable;
import java.io.File;

public interface SkriptLineConverter {
    void startClass(String name, String supertype);

    void endClass();

    void startMethod(String name, Variables variables, @Nullable SkriptType<?> returnType);

    void endMethod();

    <T> void registerVariable(String name, SkriptType<T> variable);

    void callMethod(SkriptMethod method, String line);

    void writeData(File jar);
}
