package com.bigbade.javaskript.api.skript.addon;

import com.bigbade.javaskript.api.skript.pattern.ILineParser;

import java.util.Map;

/**
 * Handles additional functionality to strings, such as string interpolation.
 */
public interface ISkriptStringAddon<T> {
    /**
     * Returns true if the addon should take over parsing
     * @param current Current character
     * @return Identified matches, the key is the starting value and the value is the length.
     */
    boolean matches(char current);

    /**
     * Parses the input to a value, instructions are a valid return value.
     * @param lineParser Line parser
     * @param lineNumber Line number
     * @param input Input string for the string addon, starting with the matched character
     * @return Parsed output
     * @see com.bigbade.javaskript.api.skript.code.ISkriptInstruction
     */
    T parse(ILineParser lineParser, int lineNumber, String input);

    /**
     * How much the parser parsed
     * @return How many characters the parser parsed
     */
    int finishedLength();
}
