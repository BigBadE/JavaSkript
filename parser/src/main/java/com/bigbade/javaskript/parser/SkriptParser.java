package com.bigbade.javaskript.parser;

import com.bigbade.javaskript.api.skript.addon.ISkriptFunctionDef;
import com.bigbade.javaskript.api.skript.code.IParsedInstruction;
import com.bigbade.javaskript.api.skript.code.ISkriptExpression;
import com.bigbade.javaskript.api.skript.defs.IParsingDef;
import com.bigbade.javaskript.api.skript.pattern.IPatternPart;
import com.bigbade.javaskript.api.skript.pattern.ISkriptPattern;
import com.bigbade.javaskript.api.skript.pattern.ParseResult;
import com.bigbade.javaskript.parser.api.SkriptFile;
import com.bigbade.javaskript.parser.exceptions.SkriptParseException;
import com.bigbade.javaskript.parser.impl.SkriptParsingDef;
import com.bigbade.javaskript.parser.parsing.LineParser;
import com.bigbade.javaskript.parser.pattern.VariablePattern;
import com.bigbade.javaskript.parser.register.AddonManager;
import com.bigbade.javaskript.parser.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SkriptParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(SkriptParser.class);
    private final SkriptFile skriptFile;

    private IParsingDef definition = null;

    public SkriptParser(String name) {
        skriptFile = new SkriptFile(name);
    }

    public SkriptFile parseSkript(File file) {
        try {
            return parseSkript(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            LOGGER.error("Error parsing Skript file", e);
        }
        return null;
    }

    @SuppressWarnings("unused")
    public SkriptFile parseSkript(Reader reader) {
        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line;
            int lineNumber = 1;
            while ((line = bufferedReader.readLine()) != null) {
                if(line.isEmpty() || line.charAt(0) == '#') {
                    lineNumber++;
                    continue;
                }
                parseLine(line, lineNumber++);
            }
        } catch (IOException e) {
            LOGGER.error("Error parsing Skript file", e);
        }
        return skriptFile;
    }

    private void parseLine(String line, int lineNumber) {
        if(StringUtil.getTabs(line) == 0 && line.charAt(line.length()-1) == ':') {
            definition = parseFunction(line.substring(0, line.length()-1), lineNumber).orElseThrow(
                    () -> new SkriptParseException(lineNumber, line, "Unknown function!"));
            return;
        }
        if(definition != null) {
            definition.parseLine(lineNumber, line);
            return;
        }
        throw new SkriptParseException(lineNumber, line, "Statement is outside of a function!");
    }

    @SuppressWarnings("unchecked")
    private <T> Optional<SkriptParsingDef<T>> parseFunction(String line, int lineNumber) {
        for(ISkriptFunctionDef addonDef : AddonManager.getAddonDefs()) {
            for(Map.Entry<ISkriptPattern, Object> entry : addonDef.getPatterns().entrySet()) {
                ParseResult result = entry.getKey().matchesInitial(line);
                if (result.getResult() != ParseResult.Result.PASSED) {
                    continue;
                }
                List<IParsedInstruction> variables = new ArrayList<>();
                for (Map.Entry<Integer, Integer> variableEntry : result.getVariables().entrySet()) {
                    IParsedInstruction variableInstruction = LineParser.parseVariable(
                            line.substring(variableEntry.getKey(), variableEntry.getValue()), lineNumber);
                    variables.add(variableInstruction);
                }
                if (!testVariables(result, variables)) {
                    return Optional.of(new SkriptParsingDef<>(addonDef, variables, (T) entry.getValue()));
                }
            }
        }
        return Optional.empty();
    }

    public static boolean testVariables(ParseResult result, List<IParsedInstruction> variables) {
        int variableIndex = 0;
        for (IPatternPart part : result.getFoundParts()) {
            if (!(part instanceof VariablePattern) || variableIndex == variables.size()
                    || !((VariablePattern) part).getType().equals(((ISkriptExpression) variables
                    .get(variableIndex++).getInstruction()).getReturnType())) {
                return true;
            }
        }
        return false;
    }
}
