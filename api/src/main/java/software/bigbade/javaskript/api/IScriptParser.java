package software.bigbade.javaskript.api;

public interface IScriptParser {
    void parse();

    void parseLine(String line, int lineNumber);

    boolean parseMethod(String line, boolean dropUnusedReturns);
}
