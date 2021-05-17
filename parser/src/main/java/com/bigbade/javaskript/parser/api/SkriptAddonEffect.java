package com.bigbade.javaskript.parser.api;

import com.bigbade.javaskript.api.skript.code.ISkriptEffect;

public class SkriptAddonEffect extends SkriptAddonInstruction implements ISkriptEffect {
    public SkriptAddonEffect(String[] patterns, Object[] patternData) {
        super(patterns, patternData);
    }
}
