package software.bigbade.javaskript.api.objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public abstract class ParsedSkriptObject {
    @Getter
    @Setter
    private ParsedSkriptObject parent;
    private final SkriptStructuredObject structuredObject;
    @Getter
    private final SkriptLineConverter converter;

    public abstract void parseLine(String line);

    public abstract void finishParsing();
}
