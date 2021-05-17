package com.bigbade.javaskript.parser.api;

import com.bigbade.javaskript.api.java.util.IClassType;
import com.bigbade.javaskript.api.skript.code.ISkriptExpression;
import com.bigbade.javaskript.parser.util.JavaClassType;
import lombok.Getter;

@Getter
public class SkriptAddonExpression extends SkriptAddonInstruction implements ISkriptExpression {
    private final IClassType returnType;

    public SkriptAddonExpression(String[] patterns, Object[] patternData) {
        super(patterns, patternData);
        returnType = new JavaClassType(SkriptAddonInstruction.getStatementMethod(getClass()).getReturnType());
        if(returnType.getSimpleName().equals("void")) {
            throw new IllegalStateException("Void expression return type at " + getClass() + "!");
        }
    }

    public SkriptAddonExpression(String... patterns) {
        this(patterns, new Object[patterns.length]);
    }
}
