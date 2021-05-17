package com.bigbade.javaskript.parser.util;

public final class StringUtil {
    private StringUtil() {}

    public static int getTabs(String line) {
        int count = 0;
        int index = 0;
        while (index < line.length()) {
            if (line.charAt(index++) == '\t') {
                count++;
            } else if (line.regionMatches(index, "    ", 0, 4)) {
                index += 4;
                count++;
            } else {
                break;
            }
        }
        return count;
    }
}
