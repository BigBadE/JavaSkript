package com.bigbade.javaskript.parser;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

class TestDocsExamples {
    @Test
    void testExpressions() {
        SkriptRunner.run("expressions",
                "on script load:\n" +
                        "    boardcast \"Expression!\"", new HashMap<>());
    }
}
