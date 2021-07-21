package com.bigbade.javaskript.parser.builtin;

import com.bigbade.javaskript.api.java.util.IClassType;
import com.bigbade.javaskript.api.skript.code.ISkriptExpression;
import com.bigbade.javaskript.api.skript.pattern.ISkriptPattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class LiteralExpression<T> implements ISkriptExpression {
    private final IClassType returnType;
    private final T value;

    @Override
    public List<IClassType> getArguments() {
        return Collections.emptyList();
    }

    @Override
    public List<ISkriptPattern> getPatterns() {
        return Collections.emptyList();
    }

    @Override
    public Method getMethod() {
        //We have a value already
        return null;
    }
}
