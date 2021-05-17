package software.bigbade.javaskript.api.objects;

import software.bigbade.javaskript.api.IScriptParser;
import software.bigbade.javaskript.api.patterns.PatternParser;

import javax.annotation.Nullable;

/**
 * !! READ ME - IMPORTANT!!
 * ALL SUBCLASSES MUST HAVE A METHOD CALLED "runMethod".
 * THIS METHOD MUST BE EFFECTIVELY STATIC, THOUGH IT DOESN'T NEED TO BE STATIC.
 */
public abstract class SkriptEffect implements SkriptMethod {
    private final PatternParser patternParser;

    public SkriptEffect(String pattern) {
        patternParser = new PatternParser(pattern);
    }

    @Nullable
    @Override
    public ParsedSkriptMethod parse(IScriptParser parser, String line) {
        if(!patternParser.parse(parser, line)) {
            return null;
        }
        return new BasicParsedSkriptMethod(this, null);
    }
}
