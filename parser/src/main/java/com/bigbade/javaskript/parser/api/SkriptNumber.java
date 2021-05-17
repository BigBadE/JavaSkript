package com.bigbade.javaskript.parser.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class SkriptNumber {
    @Getter
    private final Number number;
    @Getter
    private final NumberTypes type;

    public SkriptNumber(Number number) {
        this.number = number;
        this.type = NumberTypes.getType(number.getClass());
    }

    @RequiredArgsConstructor
    public enum NumberTypes {
        LONG(Long.TYPE),
        INTEGER(Integer.TYPE),
        SHORT(Short.TYPE),
        BYTE(Byte.TYPE),
        DOUBLE(Double.TYPE),
        FLOAT(Float.TYPE);

        private final Class<?> clazz;

        public static NumberTypes getType(Class<?> clazz) {
            for(NumberTypes types : values()) {
                if(types.clazz.equals(clazz)) {
                    return types;
                }
            }
            throw new IllegalStateException("Unknown number type " + clazz);
        }
    }
}

