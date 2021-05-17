package com.bigbade.javaskript.parser.api;

import com.bigbade.javaskript.api.java.util.IClassType;
import com.bigbade.javaskript.api.skript.code.ISkriptInstruction;
import com.bigbade.javaskript.api.skript.pattern.ISkriptPattern;
import com.bigbade.javaskript.parser.pattern.SkriptPattern;
import com.bigbade.javaskript.parser.util.JavaClassType;
import lombok.Getter;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class SkriptAddonInstruction implements ISkriptInstruction {
    private final Map<ISkriptPattern, Object> patterns = new HashMap<>();
    private final List<IClassType> arguments = new ArrayList<>();

    public SkriptAddonInstruction(String[] patterns, Object[] patternData) {
        if(patternData.length != patterns.length) {
            Object[] temp = new Object[patterns.length];
            System.arraycopy(patternData, 0, temp, 0, temp.length);
            patternData = temp;
        }
        for (int i = 0; i < patterns.length; i++) {
            this.patterns.put(new SkriptPattern(patterns[i]), patternData[i]);
        }
        for(Class<?> parameter : getStatementMethod(getClass()).getParameterTypes()) {
            arguments.add(new JavaClassType(parameter));
        }
    }

    public static Method getStatementMethod(Class<? extends ISkriptInstruction> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if(Modifier.isPublic(method.getModifiers()) && method.isAnnotationPresent(SkriptStatementMethod.class)) {
                return method;
            }
        }
        throw new IllegalStateException("No public method annotated by @SkriptStatementMethod");
    }
}
