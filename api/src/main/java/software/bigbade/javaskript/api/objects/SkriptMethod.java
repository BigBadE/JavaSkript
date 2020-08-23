package software.bigbade.javaskript.api.objects;

import software.bigbade.javaskript.api.variables.Variables;

import javax.annotation.Nullable;

public interface SkriptMethod {
    /**
     * Parses the line
     * @param line The line to parse
     * @return Whether the line fits the pattern.
     */
    @Nullable
    ParsedSkriptMethod parse(String line);

    Variables getVariables();

    Class<?> getOwner();

    String getName();

    boolean isConstructor();

    boolean isStatic();
}
