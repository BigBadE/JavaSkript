package com.bigbade.javaskript.parser.impl;

import com.bigbade.javaskript.api.java.defs.IMethodDef;
import com.bigbade.javaskript.api.java.defs.IPackageDef;
import com.bigbade.javaskript.api.skript.addon.ISkriptFunctionDef;
import com.bigbade.javaskript.api.skript.code.IParsedInstruction;
import com.bigbade.javaskript.api.skript.defs.IParsingDef;
import com.bigbade.javaskript.api.skript.defs.IValueTranslator;
import com.bigbade.javaskript.api.skript.pattern.ILineParser;
import com.bigbade.javaskript.parser.exceptions.SkriptParseException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class SkriptParsingDef implements IParsingDef {
    private static final Pattern KEY_PATTERN = Pattern.compile(":");

    private final int patternData;

    @Getter
    private final ISkriptFunctionDef functionDef;
    @Getter
    private final List<IParsedInstruction> arguments;
    @Getter
    private final Map<String, Object> keyValues = new HashMap<>();

    @Getter
    private IValueTranslator<?> currentTranslator;
    private String key = null;
    private int depth;

    public SkriptParsingDef(ISkriptFunctionDef functionDef, List<IParsedInstruction> arguments, int patternData) {
        this.functionDef = functionDef;
        this.patternData = patternData;
        this.arguments = arguments;
        currentTranslator = functionDef.getStartingTranslator();
    }

    @Override
    public void parseLine(ILineParser lineParser, int lineNumber, String line, int foundDepth) {
        if (currentTranslator != null) {
            if (foundDepth == 0) {
                keyValues.put(key, currentTranslator.getValue());
                currentTranslator = null;
            } else if (foundDepth == depth) {
                if (line.charAt(line.length() - 1) == ':') {
                    currentTranslator.startBranchFunction(lineParser, lineNumber, line.substring(0, line.length()-1));
                } else {
                    currentTranslator.readLine(lineParser, lineNumber, line.trim());
                }
            } else {
                for (int i = foundDepth; i < depth; i++) {
                    currentTranslator.endBranchFunction(lineParser, lineNumber);
                }
                currentTranslator.readLine(lineParser, lineNumber, line);
            }
            return;
        }
        findNextTranslator(lineParser, line, lineNumber, foundDepth);
    }

    @Override
    public IMethodDef locateMethod(IPackageDef packageDef) {
        if(functionDef.getStartingTranslator() != null) {
            return functionDef.locate(keyValues.get(null), patternData, packageDef);
        } else {
            return functionDef.locate(keyValues, patternData, packageDef);
        }
    }

    private void findNextTranslator(ILineParser lineParser, String line, int lineNumber, int foundDepth) {
        String[] keyValue = KEY_PATTERN.split(line, 2);
        if (keyValue.length != 2) {
            throw new SkriptParseException(lineNumber, line, "Key/value has no value, needs to be in the format " +
                    "\"key: value\"");
        }
        depth = foundDepth;
        key = keyValue[0].trim();
        if(functionDef.getTranslators() == null) {
            throw new IllegalStateException("Function " + functionDef.getClass().getSimpleName()
                    + " has no value translators!");
        }
        IValueTranslator<?> translator = functionDef.getTranslators().getOrDefault(key, null);
        if (translator == null) {
            throw new SkriptParseException(lineNumber, line, "No key with the name " + key + " found");
        }
        if(translator.readsFirstLine()) {
            translator.readLine(lineParser, lineNumber, keyValue[1].trim());
            keyValues.put(key, translator.getValue());
            key = null;
        } else {
            currentTranslator = translator;
        }
    }
}
