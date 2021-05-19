package com.bigbade.javaskript.java.expressions;

import com.bigbade.javaskript.api.skript.addon.SkriptPattern;

public class ConcatStringExpression {
    @SkriptPattern(pattern="%string%[ ]+[ }%string%")
    public String concatStrings(String first, String second) {
        return first + second;
    }
}
