package com.bigbade.javaskript.parser.impl;

import com.bigbade.javaskript.api.skript.annotations.SkriptPattern;
import com.bigbade.javaskript.api.skript.code.IBranchFunction;
import com.bigbade.javaskript.api.skript.defs.IBranchFunctionDef;
import com.bigbade.javaskript.parser.api.SkriptAddonInstruction;
import lombok.Getter;

import java.lang.reflect.Method;

public class SkriptBranchInstruction extends SkriptAddonInstruction implements IBranchFunction {
    @Getter
    private final IBranchFunctionDef functionDef;

    public SkriptBranchInstruction(IBranchFunctionDef functionDef, Method target, SkriptPattern[] patterns) {
        super(target, patterns);
        this.functionDef = functionDef;
    }
}
