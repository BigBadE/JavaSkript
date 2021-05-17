package com.bigbade.javaskript.parser.variable;

import com.bigbade.javaskript.api.java.util.IClassType;
import com.bigbade.javaskript.api.skript.variables.ISkriptVariableDef;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SkriptVariableDef implements ISkriptVariableDef {
    private final IClassType classType;
}
