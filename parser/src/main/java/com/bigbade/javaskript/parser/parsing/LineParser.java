package com.bigbade.javaskript.parser.parsing;

import com.bigbade.javaskript.api.skript.code.IParsedInstruction;
import com.bigbade.javaskript.api.skript.code.ISkriptInstruction;
import com.bigbade.javaskript.api.skript.pattern.ISkriptPattern;
import com.bigbade.javaskript.api.skript.pattern.ParseResult;
import com.bigbade.javaskript.parser.SkriptParser;
import com.bigbade.javaskript.parser.builtin.ParseVariableExpression;
import com.bigbade.javaskript.parser.exceptions.SkriptParseException;
import com.bigbade.javaskript.parser.impl.SkriptParsedInstruction;
import com.bigbade.javaskript.parser.register.AddonManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public final class LineParser {
    private static final Pattern WORD_PATTERN = Pattern.compile(" ");

    private LineParser() {
    }

    public static <T extends ISkriptInstruction> IParsedInstruction getInstruction(List<T> instructions,
                                                                                   String line, int lineNumber) {
        for (T instruction : instructions) {
            for (Map.Entry<ISkriptPattern, Object> entry : instruction.getPatterns().entrySet()) {
                ParseResult parseResult = entry.getKey().matchesInitial(line);
                if (parseResult.getResult() != ParseResult.Result.PASSED) {
                    continue;
                }
                List<IParsedInstruction> variables = new ArrayList<>();
                for (Map.Entry<Integer, Integer> variableEntry : parseResult.getVariables().entrySet()) {
                    IParsedInstruction variableInstruction = parseVariable(
                            line.substring(variableEntry.getKey(), variableEntry.getValue()), lineNumber);
                    variables.add(variableInstruction);
                }
                if (!SkriptParser.testVariables(parseResult, variables)) {
                    return new SkriptParsedInstruction(instruction, variables, entry.getValue());
                }
            }
        }
        throw new SkriptParseException(lineNumber, line, "Unknown instruction!");
    }


    public static IParsedInstruction parseString(String string, int lineNumber) {
        //TODO figure out how to implement this
        return null;
    }

    public static IParsedInstruction parseVariable(String variable, int lineNumber) {
        if (variable.charAt(0) == '"' && variable.charAt(variable.length() - 1) == '"') {
            return parseString(variable.substring(1, variable.length() - 1), lineNumber);
        } else if (variable.charAt(0) == '{' && variable.charAt(variable.length() - 1) == '}') {
            return parseSkriptVariable(variable.substring(1, variable.length()-1));
        } else {
            return getInstruction(AddonManager.getAddonExpressions(), variable, lineNumber);
        }
    }

    private static IParsedInstruction parseSkriptVariable(String variableName) {
        ParseVariableExpression.VariableTypes types;
        switch (variableName.charAt(0)) {
            case '@':
                variableName = variableName.substring(1);
                types = ParseVariableExpression.VariableTypes.OPTION;
                break;
            case '_':
                variableName = variableName.substring(1);
                types = ParseVariableExpression.VariableTypes.LOCAL;
                break;
            default:
                types = ParseVariableExpression.VariableTypes.GLOBAL;
        }
        String[] split = WORD_PATTERN.split(variableName);
        return new ParseVariableExpression(split[split.length-1], types);
    }
}
