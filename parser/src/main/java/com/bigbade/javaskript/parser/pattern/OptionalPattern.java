package com.bigbade.javaskript.parser.pattern;

import com.bigbade.javaskript.api.skript.pattern.IPatternPart;
import com.bigbade.javaskript.api.skript.pattern.ParseResult;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class OptionalPattern implements IPatternPart {
    private final List<IPatternPart> optional;

    @Override
    public ParseResult parseWord(String parsing) {
        ParseResult result = new ParseResult(ParseResult.Result.IGNORED);

        ParseResult partResult = CompiledPattern.matchesInitial(parsing, optional);
        if (partResult.getResult() == ParseResult.Result.UNDETERMINED) {
            result = partResult;
        } else if (partResult.getResult() == ParseResult.Result.PASSED) {
            return partResult;
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[");
        for (IPatternPart choice : optional) {
            builder.append(choice.toString());
        }
        return builder.append("]").toString();
    }
}
