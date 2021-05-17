package com.bigbade.javaskript.api.skript.pattern;

public interface IPatternPart {
    /**
     * Parses a list of words and returns if it matches or not.
     * @param parsing String being parsed
     * @return Parse result of those words
     */
    ParseResult parseWord(String parsing);
}
