package com.bigbade.javaskript.parser.pattern;

import com.bigbade.javaskript.api.skript.pattern.IPatternPart;
import com.bigbade.javaskript.api.skript.pattern.ISkriptPattern;
import com.bigbade.javaskript.api.skript.pattern.ParseResult;
import com.bigbade.javaskript.parser.util.SkriptTypeUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CompiledPattern implements ISkriptPattern {
    public static final Pattern SPLITTER_PATTERN = Pattern.compile("\\|");

    private final List<IPatternPart> parts = new ArrayList<>();

    @Getter
    private final int patternData;

    private int index;

    public CompiledPattern(String pattern, int patternData) {
        this.patternData = patternData;
        parts.addAll(parsePattern(pattern.trim(), -1, '\t'));
    }

    public static ParseResult matchesInitial(String matching, List<IPatternPart> parts) {
        //TODO reduce complexity and add support for comments on the line
        int index = 0;
        int looped = 0;
        int variableStart = -1;
        IPatternPart variablePart = null;
        ParseResult.ParseResultBuilder builder = new ParseResult.ParseResultBuilder();
        checkPart:
        for (IPatternPart patternPart : parts) {
            looped++;
            if (patternPart instanceof VariablePattern) {
                variableStart = index;
                variablePart = patternPart;
                continue;
            }
            int start = index;
            StringBuilder joined = new StringBuilder();
            boolean exiting = false;
            while (!exiting) {
                if (index == matching.length()) {
                    if (patternPart instanceof OptionalPattern) {
                        continue checkPart;
                    }
                    return builder.build(ParseResult.Result.UNDETERMINED);
                }
                char character = Character.toLowerCase(matching.charAt(index++));
                joined.append(character);
                ParseResult result = patternPart.parseWord(joined.toString());
                switch (result.getResult()) {
                    case IGNORED:
                        index = start;
                        exiting = true;
                        break;
                    case PASSED:
                        if (variableStart != -1) {
                            builder.addPart(variablePart);
                            builder.addParts(result.getFoundParts());
                            builder.addVariable(variableStart, index);
                            variableStart = -1;
                        }
                        exiting = true;
                        break;
                    case FAILED:
                        return result;
                    default:
                }
            }
        }
        if(variableStart >= 0) {
            builder.addPart(variablePart);
            builder.addVariable(variableStart, matching.length());
        }
        return looped == parts.size() && (variableStart != -1 || index == matching.length()) ?
                builder.build(ParseResult.Result.PASSED)
                : builder.build(ParseResult.Result.FAILED);
    }

    private List<IPatternPart> parsePattern(String pattern, int start, char exit) {
        //TODO reduce complexity
        List<IPatternPart> foundParts = new ArrayList<>();
        char current;
        StringBuilder builder = new StringBuilder();
        int currentIndex = start == -1 ? index : start;
        while (currentIndex < pattern.length() && (current = pattern.charAt(currentIndex)) != exit) {
            currentIndex++;
            switch (current) {
                case '[' -> {
                    if (!builder.isEmpty()) {
                        foundParts.add(new LiteralPattern(builder.toString()));
                    }
                    builder = new StringBuilder();
                    index++;
                    foundParts.add(new OptionalPattern(parsePattern(pattern, -1, ']')));
                    if (start == -1) {
                        currentIndex = ++index;
                    }
                }
                case '(' -> {
                    if (!builder.isEmpty()) {
                        foundParts.add(new LiteralPattern(builder.toString()));
                    }
                    builder = new StringBuilder();
                    List<List<IPatternPart>> choicePart = new ArrayList<>();
                    currentIndex += parseChoice(pattern, currentIndex, choicePart);
                    foundParts.add(new ChoicePattern(choicePart));
                }
                case '%' -> {
                    foundParts.add(new LiteralPattern(builder.toString()));
                    builder = new StringBuilder();
                    int end = pattern.indexOf('%', currentIndex);
                    if(end == -1) {
                        throw new IllegalArgumentException("Pattern has opening % but no closing!");
                    }
                    String found = pattern.substring(currentIndex, end);
                    currentIndex += found.length() + 1;
                    foundParts.add(new VariablePattern(SkriptTypeUtils.getSkriptType(found)));
                }
                default -> builder.append(current);
            }

            if (start == -1) {
                index = currentIndex;
            }
        }

        if (start != 0) {
            index = currentIndex;
            if (exit == '\t') {
                index += builder.length();
            }
        }

        if (!builder.isEmpty()) {
            foundParts.add(new LiteralPattern(builder.toString()));
        }
        return foundParts;
    }

    private int parseChoice(String pattern, int i, List<List<IPatternPart>> choicePart) {
        int skipping = 0;
        int depth = 0;
        for (String choice : SPLITTER_PATTERN.split(pattern.substring(i))) {
            int wordIndex = 0;
            for (char character : choice.toCharArray()) {
                if (character == '(') {
                    depth++;
                } else if (character == ')' && depth-- == 0) {
                    skipping += wordIndex + 2;
                    choicePart.add(parsePattern(choice.substring(0, wordIndex), 0, '\t'));
                    return skipping - 1;
                }
                wordIndex++;
            }
            skipping += choice.length() + 1;
            choicePart.add(parsePattern(choice.trim(), 0, '\t'));
        }
        throw new IllegalStateException("Choice section " + pattern + " has no end!");
    }

    @Override
    public ParseResult matchesInitial(String line) {
        return matchesInitial(line, parts);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (IPatternPart patternPart : parts) {
            builder.append(patternPart.toString());
        }
        return builder.toString();
    }
}
