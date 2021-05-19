package com.bigbade.javaskript.parser.pattern;

import com.bigbade.javaskript.api.skript.pattern.IPatternPart;
import com.bigbade.javaskript.api.skript.pattern.ISkriptPattern;
import com.bigbade.javaskript.api.skript.pattern.ParseResult;
import com.bigbade.javaskript.parser.util.SkriptClassType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CompiledPattern implements ISkriptPattern {
    public static final Pattern SPLITTER_PATTERN = Pattern.compile("\\|");

    private final List<IPatternPart> parts = new ArrayList<>();

    public CompiledPattern(String pattern) {
        parts.addAll(parsePattern(pattern.trim(), 0, '\t'));
    }

    private List<IPatternPart> parsePattern(String pattern, int start, char exit) {
        List<IPatternPart> foundParts = new ArrayList<>();
        char current;
        StringBuilder builder = new StringBuilder();
        for (int i = start; (current = pattern.charAt(i)) != exit; i++) {
            switch (current) {
                case '[':
                    foundParts.add(new OptionalPattern(parsePattern(pattern, i, ']')));
                    break;
                case '(':
                    List<IPatternPart> choicePart = new ArrayList<>();
                    i += parseChoice(pattern, i, choicePart);
                    foundParts.add(new ChoicePattern(choicePart));
                    break;
                case '%':
                    String found = pattern.substring(i);
                    i += found.length();
                    found = found.substring(0, found.indexOf('%'));
                    foundParts.add(new VariablePattern(SkriptClassType.getSkriptType(found)));
                    break;
                default:
                    builder.append(current);
            }
        }
        foundParts.add(new LiteralPattern(builder.toString()));
        return foundParts;
    }

    private int parseChoice(String pattern, int i, List<IPatternPart> choicePart) {
        int skipping = 0;
        int depth = 0;
        for (String choice : SPLITTER_PATTERN.split(pattern.substring(i))) {
            for (int j = 0; j < choice.length(); j++) {
                if (choice.charAt(j) == '(') {
                    depth++;
                } else if (choice.charAt(j) == ')' && depth-- == 0) {
                    skipping += j;
                    choicePart.addAll(parsePattern(choice.substring(0, j).trim(), 0, '\t'));
                    break;
                }
            }
            skipping += choice.length() + 1;
            choicePart.addAll(parsePattern(choice.trim(), 0, '\t'));
        }
        return skipping - 1;
    }

    @Override
    public ParseResult matchesInitial(String line) {
        return matchesInitial(line, parts);
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
