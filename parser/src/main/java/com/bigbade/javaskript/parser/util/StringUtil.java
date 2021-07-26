package com.bigbade.javaskript.parser.util;

public final class StringUtil {
    private StringUtil() {}

    public static int getTabs(String line) {
        float count = 0;
        int index = -1;
        while (++index < line.length()) {
            if (line.charAt(index) == '\t') {
                count++;
            } else if (line.charAt(index) == ' ') {
                count += .25;
            } else {
                break;
            }
        }
        return (int) count;
    }
}
