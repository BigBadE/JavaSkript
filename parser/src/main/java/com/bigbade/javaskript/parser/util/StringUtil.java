package com.bigbade.javaskript.parser.util;

public final class StringUtil {
    private StringUtil() {}

    public static int getTabs(FilePointer filePointer) {
        float count = 0;
        char character;
        while (true) {
            character = filePointer.getCharBuffer()[filePointer.bufferLocation++];
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
