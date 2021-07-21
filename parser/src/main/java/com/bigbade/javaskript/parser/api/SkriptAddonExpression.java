package com.bigbade.javaskript.parser.api;

import com.bigbade.javaskript.api.java.util.IClassType;
import com.bigbade.javaskript.api.skript.annotations.SkriptPattern;
import com.bigbade.javaskript.api.skript.code.ISkriptExpression;
import com.bigbade.javaskript.parser.util.JavaClassType;
import lombok.Getter;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

@Getter
public class SkriptAddonExpression extends SkriptAddonInstruction implements ISkriptExpression {
    private final IClassType returnType;

    public SkriptAddonExpression(Method target, SkriptPattern[] patterns) {
        super(target, patterns);
        returnType = new JavaClassType(target.getReturnType());
        if(returnType.getSimpleName().equals("void")) {
            throw new IllegalStateException("Void expression return type at " + getClass() + "!");
        }
    }

    @Nullable
    @Override
    public Object getValue() {
        //Requires direct overrides to use
        return null;
    }
}
