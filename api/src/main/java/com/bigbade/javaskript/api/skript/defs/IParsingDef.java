package com.bigbade.javaskript.api.skript.defs;

import com.bigbade.javaskript.api.java.defs.IMethodDef;
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
     * Gets the current translator of the def, this is only useful for single translator defs
     */
    IValueTranslator<?> getCurrentTranslator();

    /**
     * Parses a line of code into the Skript definition.
     * @param lineParser Line parser
     * @param lineNumber Line number of the code
     * @param line Line of code to parse
     * @param depth Amount of tabs/4 spaces in front of the line
     */
    void parseLine(ILineParser lineParser, int lineNumber, String line, int depth);

    /**
     * Locates the method def targeted by the def given the main package.
     * @param packageDef Main package
     * @return Method def target
     */
    IMethodDef locateMethod(IPackageDef packageDef);
}
