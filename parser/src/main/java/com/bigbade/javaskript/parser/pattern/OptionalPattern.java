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
        ParseResult result = CompiledPattern.matchesInitial(parsing, optional);
        return new ParseResult(result.getVariables(), result.getFoundParts(),
                result.getResult() == ParseResult.Result.FAILED ? ParseResult.Result.IGNORED : result.getResult());
    }

}
