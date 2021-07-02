package com.bigbade.javaskript.parser.impl;

import com.bigbade.javaskript.api.skript.code.IParsedInstruction;
import com.bigbade.javaskript.api.skript.defs.ICodeDef;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class SkriptCodeDef implements ICodeDef {
    @Getter
    private final List<IParsedInstruction> instructions = new ArrayList<>();

    @Override
    public void addInstruction(IParsedInstruction instruction) {
        instructions.add(instruction);
    }

    @Override
    public void execute() {
        throw new IllegalStateException("Execute not overwritten by compiler, most likely due to misuse.");
    }
}
