package com.bigbade.javaskript.parser.pattern;

import com.bigbade.javaskript.api.skript.pattern.IPatternPart;
import com.bigbade.javaskript.api.skript.pattern.ParseResult;

public class LiteralPattern implements IPatternPart {
    private final String literal;

    public LiteralPattern(String literal) {
        this.literal = literal.toLowerCase();
    }

    @Override
    public ParseResult parseWord(String word) {
        if(literal.startsWith(word.toLowerCase())) {
            return new ParseResult(this, (literal.length() == word.length()) ?
                    ParseResult.Result.PASSED : ParseResult.Result.UNDETERMINED);
        }
        return new ParseResult(ParseResult.Result.FAILED);
    }

    @Override
    public String toString() {
        return literal;
    }
}
