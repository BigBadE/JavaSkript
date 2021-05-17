package com.bigbade.javaskript.parser.impl;

import com.bigbade.javaskript.api.skript.addon.ISkriptFunctionDef;
import com.bigbade.javaskript.api.skript.code.IParsedInstruction;
import com.bigbade.javaskript.api.skript.defs.IParsingDef;
import com.bigbade.javaskript.api.skript.defs.ISkriptDef;
import com.bigbade.javaskript.api.skript.defs.IValueTranslator;
import com.bigbade.javaskript.parser.exceptions.SkriptParseException;
import com.bigbade.javaskript.parser.util.StringUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class SkriptParsingDef<T> implements IParsingDef {
    private static final Pattern KEY_PATTERN = Pattern.compile(":");

    private final ISkriptFunctionDef functionDef;
    private final T data;

    @Getter
    private final List<IParsedInstruction> arguments;

    private final Map<String, Object> keyValues = new HashMap<>();

    private IValueTranslator<?> currentTranslator;
    private String key = null;
    private int depth;

    public SkriptParsingDef(ISkriptFunctionDef functionDef, List<IParsedInstruction> arguments, T data) {
        this.functionDef = functionDef;
        this.data = data;
        this.arguments = arguments;
        currentTranslator = functionDef.getStartingTranslator();
    }

    @Override
    public void parseLine(int lineNumber, String line) {
        int foundDepth = StringUtil.getTabs(line);
        if (currentTranslator != null) {
            if (depth == foundDepth) {
                keyValues.put(key, currentTranslator.getValue());
                currentTranslator = null;
            } else {
                currentTranslator.readLine(line);
            }
        }
        String[] keyValue = KEY_PATTERN.split(line, 2);
        if (keyValue.length != 2) {
            throw new SkriptParseException(lineNumber, line, "Key/value has no value, needs to be in the format" +
                    "\"key: value\"");
        }
        depth = foundDepth;
        key = keyValue[0].trim();
        if(functionDef.getYamlValues() == null) {
            throw new IllegalStateException("Function " + functionDef.getClass().getSimpleName()
                    + " has no value translators!");
        }
        IValueTranslator<?> translator = functionDef.getYamlValues().getOrDefault(key, null);
        if (translator == null) {
            throw new SkriptParseException(lineNumber, line, "No key with the name " + key + " found");
        }
        if(translator.readsFirstLine()) {
            translator.readLine(keyValue[1].trim());
            keyValues.put(key, translator.getValue());
            key = null;
        } else {
            currentTranslator = translator;
        }
    }

    public ISkriptDef buildSkriptDef() {
        return new BuiltSkriptDef<>(keyValues, data);
    }
}
