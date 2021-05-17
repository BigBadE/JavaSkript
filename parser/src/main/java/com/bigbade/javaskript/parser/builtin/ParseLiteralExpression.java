package com.bigbade.javaskript.parser.builtin;

import com.bigbade.javaskript.api.java.util.IClassType;
import com.bigbade.javaskript.api.skript.code.ISkriptExpression;
import com.bigbade.javaskript.api.skript.pattern.ISkriptPattern;
import com.bigbade.javaskript.parser.api.SkriptStatementMethod;
import com.bigbade.javaskript.parser.impl.SkriptParsedInstruction;
import com.bigbade.javaskript.parser.util.JavaClassType;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ParseLiteralExpression<T> extends SkriptParsedInstruction {
    public ParseLiteralExpression(T literal) {
        super(new LiteralInstruction<>(literal), Collections.emptyList(), null);
    }
}

class LiteralInstruction<T> implements ISkriptExpression {
    @Getter
    private final IClassType returnType;
    private final T literal;

    public LiteralInstruction(T literal) {
        returnType = new JavaClassType(literal.getClass());
        this.literal = literal;
    }

    @SkriptStatementMethod
    public T getValue() {
        return literal;
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

