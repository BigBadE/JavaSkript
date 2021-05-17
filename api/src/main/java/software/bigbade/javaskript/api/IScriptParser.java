package software.bigbade.javaskript.api;

import software.bigbade.javaskript.api.objects.MethodLineConverter;

import java.util.Optional;

public interface IScriptParser {
    void parse();

    void parseLine(String line, int lineNumber);

    boolean parseMethod(String line, boolean dropUnusedReturns);

    Optional<MethodLineConverter<?>> getLineConverter();
}
