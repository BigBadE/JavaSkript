package com.bigbade.javaskript.api.skript.addon;

/**
 * Handles additional functionality to strings, such as string interpolation.
 */
public interface ISkriptStringAddon<T> {
    /**
     * Identifier for this string addon, like % for expressions
     * @return Identifier
     */
    String identifier();

    /**
     * Whether the string matches, and the length of the matched section if so
     * @param input Input string
     * @return Identified length of the addon, or -1 if there is no match
     */
    int matches(String input);

    /**
     * Parses the input to a value, instructions will have the instruction called and use that output value.
     * @param input Input string for the string addon
     * @return Parsed output
     * @see com.bigbade.javaskript.api.skript.code.ISkriptInstruction
     */
    T parse(String input);
}
