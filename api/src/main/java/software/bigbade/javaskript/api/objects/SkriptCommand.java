package software.bigbade.javaskript.api.objects;

import software.bigbade.javaskript.api.patterns.PatternParser;

public class SkriptCommand implements SkriptStructuredObject {
    private final PatternParser patternParser = new PatternParser("command /%string% <...>");

    @Override
    public ParsedSkriptObject parse(SkriptLineConverter converter, String line) {
        return new ParsedSkriptCommand(this, converter);
    }
}
