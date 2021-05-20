package com.bigbade.javaskript.parser;

import com.bigbade.javaskript.api.skript.pattern.ISkriptPattern;
import com.bigbade.javaskript.api.skript.pattern.ParseResult;
import com.bigbade.javaskript.parser.pattern.CompiledPattern;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PatternTests {
    @Test
    void basicPatternTest() {
        test("basic pattern", new String[] { "basic pattern" },
                new String[] { "basic patter", "basic pattern1", "basicpattern", "basi c pattern"});
    }

    @Test
    void basicChoiceTest() {
        test("(basic|simple) pattern", new String[] { "basic pattern", "simple pattern" },
                new String[] { "easy pattern" });
    }

    @Test
    void basicOptionalTest() {
        test("basic pattern[s]", new String[] { "basic pattern", "basic patterns" },
                new String[] { "basic patternss", "basic pattern[s]", "basic patternd" });
    }

    @Test
    void advancedPatternTest() {
        test("basic[ally] (a|an)[ (the|or)] pattern[s]",
                new String[] { "basic a the pattern", "basically a patterns" },
                new String[] { "basically a patternd", "basical a the pattern", "basic the pattern" });
    }

    private static void test(String pattern, String[] passing, String[] failing) {
        ISkriptPattern skriptPattern = new CompiledPattern(pattern, -1);
        Assertions.assertEquals(pattern, skriptPattern.toString());
        for(String test : passing) {
            Assertions.assertEquals(ParseResult.Result.PASSED, skriptPattern.matchesInitial(test).getResult(),
                    "Unexpected result for pattern \"" + test + "\"");
        }
        for(String test : failing) {
            Assertions.assertNotEquals(ParseResult.Result.PASSED, skriptPattern.matchesInitial(test).getResult(),
                    "Unexpected pass for pattern \"" + test + "\"");
        }
    }
}
