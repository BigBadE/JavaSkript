package com.bigbade.javaskript.parser.util;

import com.bigbade.javaskript.api.java.util.IClassType;
import lombok.Getter;

import java.util.regex.Pattern;

public final class SkriptClassType implements IClassType {
    private static final Pattern PACKAGE_PATTERN = Pattern.compile("\\.");

    @Getter
    private final String qualifiedName;
    @Getter
    private final String internalName;

    private String simpleName = null;

    public static SkriptClassType getSkriptType(String type) {
        //TODO load from pool
        return new SkriptClassType(type);
    }

    private SkriptClassType(String type) {
        //TODO load alias here
        qualifiedName = Object.class.getName();
        internalName = "Ljava.lang.Object;";
    }

    @Override
    public String getSimpleName() {
        if(simpleName != null) {
            return simpleName;
        }
        String[] split = PACKAGE_PATTERN.split(qualifiedName);
        simpleName = split[split.length-1];
        return simpleName;
    }
}
