package software.bigbade.javaskript.api.objects;

import software.bigbade.javaskript.api.IScriptParser;
import software.bigbade.javaskript.api.variables.SkriptType;

import javax.annotation.Nullable;

/**
 * !! READ ME - IMPORTANT!!
 * ALL SUBCLASSES MUST HAVE A METHOD CALLED "runMethod".
 * THIS METHOD MUST BE EFFECTIVELY STATIC, THOUGH IT DOESN'T NEED TO BE STATIC.
 */
public interface SkriptMethod {
    /**
     * Parses the line
     * @param line The line to parse
     * @return Whether the line fits the pattern.
     */
    @Nullable
    ParsedSkriptMethod parse(IScriptParser parser, String line);

    SkriptType<?>[] getVariables();
}
