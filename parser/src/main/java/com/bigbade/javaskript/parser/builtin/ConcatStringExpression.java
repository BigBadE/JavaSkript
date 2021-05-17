package com.bigbade.javaskript.parser.builtin;

import com.bigbade.javaskript.parser.api.SkriptAddonExpression;
import com.bigbade.javaskript.parser.api.SkriptStatementMethod;
import com.bigbade.javaskript.parser.register.AddonManager;

public class ConcatStringExpression extends SkriptAddonExpression {
    public static final ConcatStringExpression INSTANCE = new ConcatStringExpression();

    static {
        AddonManager.registerInstruction(INSTANCE);
    }

    private ConcatStringExpression() {
        super("%string%[ ]+[ }%string%");
    }

    @SkriptStatementMethod
    public String concatStrings(String first, String second) {
        return first + second;
    }
}
