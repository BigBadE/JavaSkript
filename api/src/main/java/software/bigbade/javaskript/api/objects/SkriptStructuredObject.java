package software.bigbade.javaskript.api.objects;

public interface SkriptStructuredObject {
    /**
     * Parses given line into the structured object
     * @param line The skript line
     * @return If the type is correct
     */
    ParsedSkriptObject parse(SkriptLineConverter converter, String line);
}
