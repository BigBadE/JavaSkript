package software.bigbade.javaskript.api.objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public abstract class ParsedSkriptObject {
    @Getter
    @Setter
    private ParsedSkriptObject parent;
    @Getter
    private final MethodLineConverter<?> converter;

    public abstract boolean shouldParseMethods();

    public abstract void parseLine(String line);

    public abstract void finishParsing();
}
