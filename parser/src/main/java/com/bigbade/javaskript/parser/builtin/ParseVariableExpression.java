package com.bigbade.javaskript.parser.builtin;

import com.bigbade.javaskript.api.java.util.IClassType;
import com.bigbade.javaskript.api.skript.code.ISkriptExpression;
import com.bigbade.javaskript.api.skript.pattern.ISkriptPattern;
import com.bigbade.javaskript.parser.impl.SkriptParsedInstruction;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ParseVariableExpression extends SkriptParsedInstruction  {
    public ParseVariableExpression(String variable, VariableTypes types) {
        super(new VariableInstruction(variable, types), Collections.emptyList(), null);
    }

    public enum VariableTypes {
        GLOBAL,
        LOCAL,
        OPTION
    }
}

class VariableInstruction implements ISkriptExpression {
    public VariableInstruction(String variable, ParseVariableExpression.VariableTypes type) {
        //TODO fetch the variable type, support %expressions% in the variable name
    }

    @Override
    public IClassType getReturnType() {
        return null;
    }

    @Override
    public List<IClassType> getArguments() {
        return Collections.emptyList();
    }

    @Override
    public Map<ISkriptPattern, Object> getPatterns() {
        return Collections.emptyMap();
    }
}
