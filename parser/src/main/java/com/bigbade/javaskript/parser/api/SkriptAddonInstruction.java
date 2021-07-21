package com.bigbade.javaskript.parser.api;

import com.bigbade.javaskript.api.java.util.IClassType;
import com.bigbade.javaskript.api.skript.annotations.SkriptPattern;
import com.bigbade.javaskript.api.skript.code.ISkriptInstruction;
import com.bigbade.javaskript.api.skript.pattern.ISkriptPattern;
import com.bigbade.javaskript.parser.pattern.CompiledPattern;
import com.bigbade.javaskript.parser.util.JavaClassType;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Getter
public class SkriptAddonInstruction implements ISkriptInstruction {
    private final List<ISkriptPattern> patterns = new ArrayList<>();
    private final List<IClassType> arguments = new ArrayList<>();
    private final Method method;

    public SkriptAddonInstruction(Method target, SkriptPattern[] patterns) {
        this.method = target;
        for (SkriptPattern pattern : patterns) {
            this.patterns.add(new CompiledPattern(pattern.pattern(), pattern.patternData()));
        }
        for(Class<?> parameter : target.getParameterTypes()) {
            arguments.add(new JavaClassType(parameter));
        }
    }
}
