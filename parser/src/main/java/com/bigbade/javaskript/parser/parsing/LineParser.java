package com.bigbade.javaskript.parser.parsing;

import com.bigbade.javaskript.api.skript.code.IParsedInstruction;
import com.bigbade.javaskript.api.skript.code.ISkriptInstruction;
import com.bigbade.javaskript.api.skript.defs.IBranchFunctionDef;
import com.bigbade.javaskript.api.skript.pattern.ILineParser;
import com.bigbade.javaskript.api.skript.pattern.ISkriptPattern;
import com.bigbade.javaskript.api.skript.pattern.ParseResult;
import com.bigbade.javaskript.parser.SkriptParser;
import com.bigbade.javaskript.parser.exceptions.SkriptParseException;
import com.bigbade.javaskript.parser.impl.SkriptParsedBranchInstruction;
import com.bigbade.javaskript.parser.impl.SkriptParsedInstruction;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class LineParser implements ILineParser {
    @Getter
    private final AddonManager addonManager = new AddonManager();

    public static IParsedInstruction parseString(String string, int lineNumber) {
        //TODO figure out how to implement this
        return null;
    }

    private static IParsedInstruction parseSkriptVariable(String variableName) {
        //TODO
        return null;
    }

    public IParsedInstruction getInstruction(String line, int lineNumber) {
        return getInstruction(addonManager.getAddonInstructions(), line, lineNumber);
    }

    public <T extends ISkriptInstruction, E extends IParsedInstruction> E getInstruction(List<T> instructions,
                                                                                         String line, int lineNumber) {
        for (T instruction : instructions) {
            for (ISkriptPattern pattern : instruction.getPatterns()) {
                ParseResult parseResult = pattern.matchesInitial(line);
                if (parseResult.getResult() != ParseResult.Result.PASSED) {
                    continue;
                }
                List<IParsedInstruction> variables = new ArrayList<>();
                for (Map.Entry<Integer, Integer> variableEntry : parseResult.getVariables().entrySet()) {
                    IParsedInstruction variableInstruction = parseVariable(
                            line.substring(variableEntry.getKey(), variableEntry.getValue()), lineNumber);
                    variables.add(variableInstruction);
                }
                if (SkriptParser.testVariables(parseResult, variables)) {
                    //Hardcoded check of generic type,
                    if (instruction instanceof IBranchFunctionDef) {
                        //noinspection unchecked
                        return (E) new SkriptParsedBranchInstruction(instruction, variables, pattern.getPatternData());
                    } else {
                        //noinspection unchecked
                        return (E) new SkriptParsedInstruction(instruction, variables, pattern.getPatternData());
                    }
                }
            }
        }
        throw new SkriptParseException(lineNumber, line, "Unknown instruction!");
    }

    public IParsedInstruction parseVariable(String variable, int lineNumber) {
        if (variable.charAt(0) == '"' && variable.charAt(variable.length() - 1) == '"') {
            return parseString(variable.substring(1, variable.length() - 1), lineNumber);
        } else if (variable.charAt(0) == '{' && variable.charAt(variable.length() - 1) == '}') {
            return parseSkriptVariable(variable.substring(1, variable.length() - 1));
        } else {
            return getInstruction(addonManager.getAddonExpressions(), variable, lineNumber);
        }
    }
}
