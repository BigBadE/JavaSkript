package com.bigbade.javaskript.parser;

import com.bigbade.javaskript.parser.util.FilePointer;
import com.bigbade.javaskript.parser.util.StringUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StringUtilTest {
    @Test
    public void testGetTabs() {
        testFilePointer(2, "\t\ttest");
        testFilePointer(3, "\t\t    test");
        testFilePointer(3, "\t    \ttest");
        testFilePointer(3, "    \t\ttest");
        testFilePointer(3, "    \t    test");
        testFilePointer(3, "     \t   test");
        testFilePointer(2, "        test");
    }

    private static void testFilePointer(int expected, String string) {
        Assertions.assertEquals(expected, StringUtil.getTabs(string));
    }
}
