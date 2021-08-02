package com.bigbade.javaskript.parser.util;

import com.bigbade.javaskript.api.java.util.IClassType;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;

@RequiredArgsConstructor
public class JavaClassType implements IClassType {
    @Nullable
    private final Class<?> clazz;
    private String internalName = null;

    @Override
    public String getSimpleName() {
        return clazz == null ? "void" : clazz.getSimpleName();
    }

    @Override
    public String getQualifiedName() {
        return clazz == null ? "void" : clazz.getName();
    }

    @Override
    public String getInternalName() {
        if (internalName != null) {
            return internalName;
        }
        if (clazz == null) {
            return "V";
        }
        if (clazz.isPrimitive()) {
            if (clazz == Void.TYPE) {
                internalName = "V";
            } else if (clazz == Boolean.TYPE) {
                internalName = "Z";
            } else if (clazz == Character.TYPE) {
                internalName = "C";
            } else if (clazz == Byte.TYPE) {
                internalName = "B";
            } else if (clazz == Short.TYPE) {
                internalName = "S";
            } else if (clazz == Integer.TYPE) {
                internalName = "I";
            } else if (clazz == Float.TYPE) {
                internalName = "F";
            } else if (clazz == Long.TYPE) {
                internalName = "J";
            } else if (clazz == Double.TYPE) {
                internalName = "D";
            }
        }
        internalName = "L" + getQualifiedName() + ";";
        return internalName;
    }

    public boolean isOfType(IClassType other) {
        if (!(other instanceof JavaClassType javaClassType))
            throw new IllegalArgumentException("Unknown class type " + other);
        return (clazz == null && javaClassType.clazz == null) ||
                (clazz != null && javaClassType.clazz != null && clazz.isAssignableFrom(javaClassType.clazz));
    }
}
