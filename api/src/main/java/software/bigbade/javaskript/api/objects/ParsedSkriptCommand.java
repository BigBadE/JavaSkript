package software.bigbade.javaskript.api.objects;

import java.util.HashMap;
import java.util.Map;

public class ParsedSkriptCommand extends ParsedSkriptObject {
    private final Map<String, String> commandData = new HashMap<>();

    public ParsedSkriptCommand(SkriptStructuredObject methodObject, SkriptLineConverter converter) {
        super(methodObject, converter);
    }

    @Override
    public void parseLine(String line) {

    }

    @Override
    public void finishParsing() {

    }
}
