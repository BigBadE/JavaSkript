package com.bigbade.javaskript.parser.exceptions;

public class SkriptParseException extends RuntimeException {
    public SkriptParseException(int line, String code, String error) {
        super(error + " at line " + line + ": " + code);
    }
}
