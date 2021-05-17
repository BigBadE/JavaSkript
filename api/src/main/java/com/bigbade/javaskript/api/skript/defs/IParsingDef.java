package com.bigbade.javaskript.api.skript.defs;

public interface IParsingDef {
    /**
     * Parses a line of code into the Skript definition.
     * @param lineNumber Line number of the code
     * @param line Line of code to parse
     */
    void parseLine(int lineNumber, String line);

    /**
     * Builds the parsing def into a full Skript definition.
     * @return Built Skript def
     */
    ISkriptDef buildSkriptDef();
}
