
package com.bigbade.javaskript.parser;

import com.bigbade.javaskript.api.skript.addon.ISkriptFunctionDef;
import com.bigbade.javaskript.api.skript.code.IParsedInstruction;
import com.bigbade.javaskript.api.skript.code.ISkriptExpression;
import com.bigbade.javaskript.api.skript.defs.IParsingDef;
import com.bigbade.javaskript.api.skript.parser.ISkriptParser;
import com.bigbade.javaskript.api.skript.pattern.IPatternPart;
import com.bigbade.javaskript.api.skript.pattern.ISkriptPattern;
import com.bigbade.javaskript.api.skript.pattern.ParseResult;
import com.bigbade.javaskript.parser.api.SkriptFile;
import com.bigbade.javaskript.parser.exceptions.SkriptParseException;
import com.bigbade.javaskript.parser.impl.SkriptParsingDef;
import com.bigbade.javaskript.parser.parsing.LineParser;
import com.bigbade.javaskript.parser.pattern.VariablePattern;
import com.bigbade.javaskript.parser.util.StringUtil;
import lombok.Getter;
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

public class SkriptParser implements ISkriptParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(SkriptParser.class);
    @Getter
    private final LineParser lineParser = new LineParser();

    private IParsingDef function = null;

    /**
     * Compares a parse result against passed variables
     * @param result Parse result
     * @param variables Variables to compare against
     * @return Whether the variables match the ones required in the parse result
     */
    public static boolean testVariables(ParseResult result, List<IParsedInstruction> variables) {
        int variableIndex = 0;
        for (IPatternPart part : result.getFoundParts()) {
            if (!(part instanceof VariablePattern) || variableIndex == variables.size()
                    || !((VariablePattern) part).getType().isOfType(
                    ((ISkriptExpression) variables.get(variableIndex++).getInstruction()).getReturnType())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Parses a skript from a file
     * @param file File to parse
     * @return Skript file
     */
    @SuppressWarnings("unused")
    public SkriptFile parseSkript(File file) {
        try {
            return parseSkript(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8),
                    file.getName().substring(0, file.getName().length()-3));
        } catch (FileNotFoundException e) {
            LOGGER.error("Error parsing Skript file", e);
        }
        return null;
    }

    /**
     * Parses a skript from a reader into a skript file
     * @param reader Reader to parse from
     * @return Skript file
     */
    public SkriptFile parseSkript(Reader reader, String name) {
        SkriptFile skriptFile = new SkriptFile(name);
        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line;
            int lineNumber = 1;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.isEmpty() || line.charAt(0) == '#') {
                    lineNumber++;
                    continue;
                }
                parseLine(skriptFile, line, lineNumber++);
            }
            if (function != null) {
                skriptFile.addParsedFunction(function);
            }
        } catch (IOException e) {
            LOGGER.error("Error parsing Skript file", e);
        }
        return skriptFile;
    }

    /**
     * Parses a line and adds it into the Skript file
     * @param line Line to parse
     * @param lineNumber Line number
     * @throws SkriptParseException if there is code without a function
     */
    private void parseLine(SkriptFile skriptFile, String line, int lineNumber) {
        String trimmed = line.trim();
        if(trimmed.charAt(0) == '#') return;
        int depth = StringUtil.getTabs(line);
        if (depth == 0) {
            if (function != null) {
                skriptFile.addParsedFunction(function);
            }
            function = parseFunction(trimmed.substring(0, trimmed.length() - 1), lineNumber);
            return;
        }
        if (function != null) {
            function.parseLine(lineParser, lineNumber, trimmed, depth);
            return;
        }
        throw new SkriptParseException(lineNumber, trimmed, "Statement is outside of a function!");
    }

    /**
     * Parses a function line into a parsed def
     * @param line Line with the function
     * @param lineNumber Line number
     * @return Function found
     * @throws SkriptParseException if no function is found
     */
    private SkriptParsingDef parseFunction(String line, int lineNumber) {
        for (ISkriptFunctionDef addonDef : lineParser.getAddonManager().getAddonDefs()) {
            for (ISkriptPattern pattern : addonDef.getPatterns()) {
                ParseResult result = pattern.matchesInitial(line);
                if (result.getResult() != ParseResult.Result.PASSED) {
                    continue;
                }
                int i = 0;
                IPatternPart part;
                List<IParsedInstruction> variables = new ArrayList<>();
                for (Map.Entry<Integer, Integer> variableEntry : result.getVariables().entrySet()) {
                    do {
                        part = result.getFoundParts().get(i++);
                    } while (!(part instanceof VariablePattern));
                    IParsedInstruction variableInstruction = lineParser.parseVariable(part,
                            line.substring(variableEntry.getKey(), variableEntry.getValue()), lineNumber);
                    variables.add(variableInstruction);
                }
                if (testVariables(result, variables)) {
                    return new SkriptParsingDef(addonDef, variables, pattern.getPatternData());
                }
            }
        }
        throw new SkriptParseException(lineNumber, line, "Unknown function");
    }
}
