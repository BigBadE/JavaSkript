package com.bigbade.javaskript.parser.parsing;

import com.bigbade.javaskript.api.skript.addon.ISkriptLiteralAddon;
import com.bigbade.javaskript.api.skript.code.IParsedInstruction;
import com.bigbade.javaskript.api.skript.code.ISkriptInstruction;
import com.bigbade.javaskript.api.skript.defs.IBranchFunctionDef;
import com.bigbade.javaskript.api.skript.pattern.ILineParser;
import com.bigbade.javaskript.api.skript.pattern.IPatternPart;
import com.bigbade.javaskript.api.skript.pattern.ISkriptPattern;
import com.bigbade.javaskript.api.skript.pattern.ParseResult;
import com.bigbade.javaskript.parser.SkriptParser;
import com.bigbade.javaskript.parser.builtin.LiteralExpression;
import com.bigbade.javaskript.parser.exceptions.SkriptParseException;
import com.bigbade.javaskript.parser.impl.SkriptParsedBranchInstruction;
import com.bigbade.javaskript.parser.impl.SkriptParsedInstruction;
import com.bigbade.javaskript.parser.pattern.VariablePattern;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
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
                int i = 0;
                IPatternPart part;
                List<IParsedInstruction> variables = new ArrayList<>();
                for (Map.Entry<Integer, Integer> variableEntry : parseResult.getVariables().entrySet()) {
                    do {
                        part = parseResult.getFoundParts().get(i++);
                    } while (!(part instanceof VariablePattern));
                    IParsedInstruction variableInstruction = parseVariable(part,
                            line.substring(variableEntry.getKey(), variableEntry.getValue()), lineNumber);
                    variables.add(variableInstruction);
                }
                if (SkriptParser.testVariables(parseResult, variables)) {
                    //Hardcoded check of generic type
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

    public IParsedInstruction parseVariable(IPatternPart part, String variable, int lineNumber) {
        if (variable.charAt(0) == '"' && variable.charAt(variable.length() - 1) == '"') {
            return parseString(variable.substring(1, variable.length() - 1), lineNumber);
        } else if (variable.charAt(0) == '{' && variable.charAt(variable.length() - 1) == '}') {
            return parseSkriptVariable(variable.substring(1, variable.length() - 1));
        } else {
            for(ISkriptLiteralAddon<?> literalAddon : addonManager.getLiteralAddons()) {
                if(literalAddon.getClassType().equals(((VariablePattern) part).getType()) &&
                        literalAddon.matches(variable)) {
                    return new SkriptParsedInstruction(new LiteralExpression<>(literalAddon.getClassType(),
                            literalAddon.getValue(variable)), Collections.emptyList(), 0);
                }
            }
            return getInstruction(addonManager.getAddonExpressions(), variable, lineNumber);
        }
    }
}
