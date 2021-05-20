package com.bigbade.javaskript.parser.pattern;

import com.bigbade.javaskript.api.java.util.IClassType;
import com.bigbade.javaskript.api.skript.pattern.IPatternPart;
import com.bigbade.javaskript.api.skript.pattern.ParseResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VariablePattern implements IPatternPart {
    @Getter
    private final IClassType type;

    @Override
    public ParseResult parseWord(String word) {
        throw new IllegalStateException("Cannot parse variable as word!");
    }

    @Override
    public String toString() {
        return "%" + type.getSimpleName() + "%";
    }
}
