package software.bigbade.javaskript.api.objects;

import software.bigbade.javaskript.api.IScriptParser;

public interface SkriptStructuredObject {
    /**
     * Parses given line into the structured object
     * @param line The skript line
     * @return If the type is correct
     */
    ParsedSkriptObject parse(IScriptParser parser, SkriptLineConverter converter, String line);
}
