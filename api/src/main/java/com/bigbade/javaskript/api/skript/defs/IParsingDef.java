package com.bigbade.javaskript.api.skript.defs;

import com.bigbade.javaskript.api.java.defs.IPackageDef;
import com.bigbade.javaskript.api.skript.addon.ISkriptFunctionDef;
import com.bigbade.javaskript.api.skript.pattern.ILineParser;

import java.util.Map;

/**
 * A def being parsed, handles building the def
 */
public interface IParsingDef {
    /**
     * Gets the skript function of the def
     * @return Function of the def
     */
    ISkriptFunctionDef getFunctionDef();

    /**
     * Gets the key/value arguments of the def
     * @return Arguments of the def
     */
    Map<String, Object> getKeyValues();

    /**
     * Parses a line of code into the Skript definition.
     * @param lineParser Line parser
     * @param lineNumber Line number of the code
     * @param line Line of code to parse
     */
    void parseLine(ILineParser lineParser, int lineNumber, String line);

    /**
     * Finalized the parsing def
     */
    void finalizeParsingDef(IPackageDef packageDef);
}
