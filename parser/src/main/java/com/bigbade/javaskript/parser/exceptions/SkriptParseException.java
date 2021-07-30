package com.bigbade.javaskript.parser.exceptions;

import com.bigbade.javaskript.parser.util.FilePointer;

public class SkriptParseException extends RuntimeException {
    public SkriptParseException(FilePointer filePointer, String error) {
        super(error + " at line " + filePointer.getLineNumber() + ": " +
                new String(filePointer.getCharBuffer(), filePointer.getLineStart(), filePointer.bufferLocation));
    }
}
