package com.bigbade.javaskript.parser.util;

public final class StringUtil {
    private StringUtil() {}

<<<<<<< HEAD
    public static int getTabs(FilePointer filePointer) {
        float count = 0;
        char character;
        while (true) {
            character = filePointer.getCharBuffer()[filePointer.bufferLocation++];
            if (character == '\t') {
                count++;
            } else if (character == ' ') {
                count += .25;
=======
    public static int getTabs(String line) {
        int count = 0;
        int index = 0;
        while (index < line.length()) {
            if (line.charAt(index++) == '\t') {
                count++;
            } else if (line.regionMatches(index, "    ", 0, 4)) {
                index += 4;
                count++;
>>>>>>> parent of 2c69850... Set up parser integration test, bug fixing time
            } else {
                break;
            }
        }
        return count;
    }
}
