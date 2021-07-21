package com.bigbade.javaskript.parser.api;

import com.bigbade.javaskript.api.skript.annotations.SkriptPattern;
import com.bigbade.javaskript.api.skript.code.ISkriptEffect;

import java.lang.reflect.Method;

public class SkriptAddonEffect extends SkriptAddonInstruction implements ISkriptEffect {
    public SkriptAddonEffect(Method target, SkriptPattern[] patterns) {
        super(target, patterns);
    }
}
