package software.bigbade.javaskript.api.patterns;

import software.bigbade.javaskript.api.variables.SkriptType;

import javax.annotation.Nullable;

public interface PatternType {
    void parseString(String input);

    @Nullable
    SkriptType<?> getType();

    boolean matches(String input);

    int getTotalSize();
}
