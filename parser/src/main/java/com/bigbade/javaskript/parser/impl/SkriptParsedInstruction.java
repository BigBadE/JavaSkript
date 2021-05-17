package com.bigbade.javaskript.parser.impl;

import com.bigbade.javaskript.api.skript.code.IParsedInstruction;
import com.bigbade.javaskript.api.skript.code.ISkriptInstruction;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class SkriptParsedInstruction implements IParsedInstruction {
    private final ISkriptInstruction instruction;
    private final List<IParsedInstruction> parsedArguments;
    private final Object patternData;
}
