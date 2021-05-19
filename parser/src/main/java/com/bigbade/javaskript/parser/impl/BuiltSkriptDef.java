package com.bigbade.javaskript.parser.impl;

import com.bigbade.javaskript.api.skript.defs.ISkriptDef;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class BuiltSkriptDef implements ISkriptDef {
    @Getter
    private final Map<String, ?> keyValues;

    private final int patternData;

    @Override
    public int getPatternData() {
        return patternData;
    }
}
