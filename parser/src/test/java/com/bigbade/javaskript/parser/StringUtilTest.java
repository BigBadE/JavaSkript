package com.bigbade.javaskript.parser;

import com.bigbade.javaskript.parser.util.StringUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StringUtilTest {
    @Test
    public void testGetTabs() {
        Assertions.assertEquals(2, StringUtil.getTabs("\t\ttest"));
        Assertions.assertEquals(3, StringUtil.getTabs("\t\t    test"));
        Assertions.assertEquals(3, StringUtil.getTabs("\t    \ttest"));
        Assertions.assertEquals(3, StringUtil.getTabs("    \t\ttest"));
        Assertions.assertEquals(3, StringUtil.getTabs("    \t    test"));
        Assertions.assertEquals(3, StringUtil.getTabs("     \t   test"));
        Assertions.assertEquals(2, StringUtil.getTabs("        test"));
    }
}
