package com.bigbade.javaskript.api.skript.pattern;

/**
 * Matches a line to statements, expressions, and functions
 */
@SuppressWarnings("unused")
public interface ISkriptPattern {
    /**
     * Checks if the pattern matches literals, optionals, and choices, but not variables.
     * @param line Line to compare against
     * @return ParseResult with the appropriate data
     */
    ParseResult matchesInitial(String line);

    /**
     * Gets the pattern data of the pattern
     * @return Pattern data
     */
    int getPatternData();
}
