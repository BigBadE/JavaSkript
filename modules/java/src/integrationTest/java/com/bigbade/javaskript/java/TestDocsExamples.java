package com.bigbade.javaskript.java;

import com.bigbade.javaskript.api.skript.code.IParsedInstruction;
import com.bigbade.javaskript.java.defs.ScriptLoadDef;
import com.bigbade.javaskript.java.effects.BroadcastExpression;
import com.bigbade.javaskript.java.util.SkriptRunner;
import com.bigbade.javaskript.parser.builtin.LiteralExpression;
import com.bigbade.javaskript.parser.parsing.CodeTranslator;
import org.junit.jupiter.api.Test;

import java.util.List;

class TestDocsExamples {
    @Test
    void testExpressions() {
        new SkriptRunner()
                .startDefClass(ScriptLoadDef.class)
                .addArgument(null, code -> {
                    List<IParsedInstruction> instructions = ((CodeTranslator) code).getValue().getInstructions();
                    if (instructions.size() != 1 ||
                            !(instructions.get(0).getInstruction().getMethod().getDeclaringClass()
                                    .equals(BroadcastExpression.class))) return false;
                    List<IParsedInstruction> args = instructions.get(0).getParsedArguments();
                    if (args.size() != 1 || args.get(0).getInstruction() instanceof LiteralExpression) return false;
                    return true;
                })
                .run("expressions",
                        "on script load:\n" +
                                "    broadcast \"Expression!\"");
    }
}
