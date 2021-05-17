package com.bigbade.javaskript.parser.pattern;

import com.bigbade.javaskript.api.skript.pattern.IPatternPart;
import com.bigbade.javaskript.api.skript.pattern.ParseResult;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ChoicePattern implements IPatternPart {
    private final List<IPatternPart> choices;

    @Override
    public ParseResult parseWord(String word) {
        int undetermined = 0;
        for (IPatternPart part : choices) {
            ParseResult parseReturn = part.parseWord(word);
            if (parseReturn.getResult() == ParseResult.Result.PASSED) {
                return parseReturn;
            } else if (parseReturn.getResult() == ParseResult.Result.FAILED) {
                undetermined++;
            }
        }
        if (undetermined > 0) {
            return new ParseResult(ParseResult.Result.UNDETERMINED);
        }
        return new ParseResult(ParseResult.Result.FAILED);
    }
}
