package com.bigbade.javaskript.parser.pattern;

import com.bigbade.javaskript.api.skript.pattern.IPatternPart;
import com.bigbade.javaskript.api.skript.pattern.ParseResult;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ChoicePattern implements IPatternPart {
    private final List<List<IPatternPart>> choices;

    @Override
    public ParseResult parseWord(String word) {
        int failed = 0;
        for (List<IPatternPart> parts : choices) {
            ParseResult parseReturn = CompiledPattern.matchesInitial(word, parts);
            if (parseReturn.getResult() == ParseResult.Result.PASSED) {
                return parseReturn;
            } else if (parseReturn.getResult() == ParseResult.Result.FAILED) {
                failed++;
            }
        }

        if (failed < choices.size()) {
            return new ParseResult(ParseResult.Result.UNDETERMINED);
        }
        return new ParseResult(ParseResult.Result.FAILED);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("(");
        for(List<IPatternPart> part : choices) {
            for(IPatternPart choice : part) {
                builder.append(choice.toString());
            }
            builder.append("|");
        }
        return builder.deleteCharAt(builder.length()-1).append(")").toString();
    }
}
