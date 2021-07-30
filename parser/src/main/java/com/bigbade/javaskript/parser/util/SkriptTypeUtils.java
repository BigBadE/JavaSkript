package com.bigbade.javaskript.parser.util;

import com.bigbade.javaskript.api.java.util.IClassType;
import lombok.Getter;

import java.util.regex.Pattern;

public final class SkriptTypeUtils {
    private SkriptTypeUtils() {}

    public static IClassType getSkriptType(String type) {
        //TODO replace with aliases and load from pool
        return new JavaClassType(Object.class);
        /*try {
            return new JavaClassType(Class.forName(type.substring(1, type.length()-1)));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unknown type " + type);
        }*/
    }
}
