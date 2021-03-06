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
        if(internalName != null) {
            return internalName;
        }
        if(clazz == null) {
            return "V";
        }
        if(clazz.isPrimitive()) {
            if(clazz == Void.TYPE) {
                internalName = "V";
            } else if(clazz == Boolean.TYPE) {
                internalName = "Z";
            } else if(clazz == Character.TYPE) {
                internalName = "C";
            }
        }
        internalName = "L" + getQualifiedName() + ";";
        return internalName;
    }
}
