package com.bigbade.javaskript.parser.pattern;

import com.bigbade.javaskript.api.skript.pattern.IPatternPart;
import com.bigbade.javaskript.api.skript.pattern.ParseResult;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class OptionalPattern implements IPatternPart {
    private final List<IPatternPart> optional;

    @Override
    public ParseResult parseWord(String parsing) {
        ParseResult result = matchesInitial(parsing, optional);
        return new ParseResult(result.getVariables(), result.getFoundParts(),
                result.getResult() == ParseResult.Result.FAILED ? ParseResult.Result.IGNORED : result.getResult());
    }

    public static ParseResult matchesInitial(String matching, List<IPatternPart> parts) {
        int index = 0;
        int looped = 0;
        int variableStart = -1;
        ParseResult.ParseResultBuilder builder = new ParseResult.ParseResultBuilder();
        for (IPatternPart patternPart : parts) {
            looped++;
            if (patternPart instanceof VariablePattern) {
                variableStart = index;
                continue;
            }
            int start = index;
            StringBuilder joined = new StringBuilder();
            boolean exiting = false;
            while (!exiting) {
                if(index == matching.length()) {
                    return builder.build(ParseResult.Result.UNDETERMINED);
                }
                joined.append(matching.charAt(index++));
                ParseResult result = patternPart.parseWord(joined.toString());
                switch (result.getResult()) {
                    case IGNORED:
                        index = start;
                        exiting = true;
                        break;
                    case PASSED:
                        if(variableStart != -1) {
                            builder.addParts(result.getFoundParts());
                            builder.addVariable(variableStart, index);
                            variableStart = -1;
                        }
                        exiting = true;
                        break;
                    case FAILED:
                        index = start + 1;
                        exiting = true;
                        break;
                    default:
                }
            }
        }
        return looped == parts.size() ? builder.build(ParseResult.Result.PASSED)
                : builder.build(ParseResult.Result.FAILED);
    }
}
