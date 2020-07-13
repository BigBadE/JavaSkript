package software.bigbade.javaskript.api.objects;

import software.bigbade.javaskript.api.SkriptLineConverter;

public interface SkriptStructuredObject {
    /**
     * Parses given line into the structured object
     * @param line The skript line
     * @return If the type is correct
     */
    boolean parse(String line);

    void register(SkriptLineConverter lineConverter, String line);
}
