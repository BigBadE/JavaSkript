package com.bigbade.javaskript.parser.util;

public final class StringUtil {
    private StringUtil() {}

    public static int getTabs(String line) {
        float count = 0;
        int index = 0;
        char character;
        while (true) {
            character = line.charAt(index++);
            if (character == '\t') {
                count++;
            } else if (character == ' ') {
                count += .25;
            } else {
                break;
            }
        }
        return (int) count;
    }
}
