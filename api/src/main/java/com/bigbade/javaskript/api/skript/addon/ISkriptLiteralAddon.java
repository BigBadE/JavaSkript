package com.bigbade.javaskript.api.skript.addon;

/**
 * Handles additional literals, such as strings or numbers.
 */
public interface ISkriptLiteralAddon<T> {
    /**
     * Returns true if the value is valid for this addon
     * @param value String to check against
     * @return If the string matches
     */
    boolean matches(String value);

    /**
     * Gets the value from the literal
     * @param input String to get the value of
     * @return Parsed value of the literal
     */
    T getValue(String input);
}
