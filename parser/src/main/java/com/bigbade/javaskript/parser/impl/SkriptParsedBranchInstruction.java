package com.bigbade.javaskript.parser.impl;

import com.bigbade.javaskript.api.skript.code.IParsedBranchFunction;
import com.bigbade.javaskript.api.skript.code.IParsedInstruction;
import com.bigbade.javaskript.api.skript.code.ISkriptInstruction;
import com.bigbade.javaskript.api.skript.defs.ICodeDef;
import lombok.Getter;

import java.util.List;

public class SkriptParsedBranchInstruction extends SkriptParsedInstruction implements IParsedBranchFunction {
    @Getter
    private final ICodeDef codeDef = new SkriptCodeDef();

    public SkriptParsedBranchInstruction(ISkriptInstruction instruction, List<IParsedInstruction> parsedArguments, Integer patternData) {
        super(instruction, parsedArguments, patternData);
    }
}
