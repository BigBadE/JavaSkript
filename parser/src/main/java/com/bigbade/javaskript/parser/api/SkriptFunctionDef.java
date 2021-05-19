package com.bigbade.javaskript.parser.api;

import com.bigbade.javaskript.api.skript.addon.ISkriptFunctionDef;
import com.bigbade.javaskript.api.skript.defs.IValueTranslator;
import com.bigbade.javaskript.api.skript.pattern.ISkriptPattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
@RequiredArgsConstructor
public abstract class SkriptFunctionDef implements ISkriptFunctionDef {
    @Getter
    private final Map<String, IValueTranslator<Object>> yamlValues;

    @Getter
    private final Map<ISkriptPattern, Object> patterns;
}
