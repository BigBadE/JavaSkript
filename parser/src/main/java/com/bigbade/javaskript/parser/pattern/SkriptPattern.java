package com.bigbade.javaskript.parser.pattern;

import com.bigbade.javaskript.api.skript.pattern.IPatternPart;
import com.bigbade.javaskript.api.skript.pattern.ISkriptPattern;
import com.bigbade.javaskript.api.skript.pattern.ParseResult;
import com.bigbade.javaskript.parser.util.SkriptClassType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class SkriptPattern implements ISkriptPattern {
    public static final Pattern WORD_PATTERN = Pattern.compile(" ");
    public static final Pattern SPLITTER_PATTERN = Pattern.compile("\\|");

    private final List<IPatternPart> parts = new ArrayList<>();

    public SkriptPattern(String pattern) {
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
        return OptionalPattern.matchesInitial(line, parts);
    }
}
