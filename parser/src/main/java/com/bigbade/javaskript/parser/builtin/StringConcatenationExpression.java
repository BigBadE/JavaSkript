package com.bigbade.javaskript.parser.builtin;

import com.bigbade.javaskript.api.java.util.IClassType;
import com.bigbade.javaskript.api.skript.code.ISkriptExpression;
import com.bigbade.javaskript.api.skript.pattern.ISkriptPattern;
import com.bigbade.javaskript.parser.util.JavaClassType;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class StringConcatenationExpression implements ISkriptExpression {
    private static final Method METHOD = getTargetMethod();
    private static final JavaClassType RETURN_TYPE = new JavaClassType(String.class);

    //Ignore the NoSuchMethodException cause we know it's there
    //Put in a method cause we can't use SneakyThrows on a field
    @SneakyThrows(NoSuchMethodException.class)
    private static Method getTargetMethod() {
        return StringConcatenationExpression.class
                .getDeclaredMethod("concatenate", ISkriptExpression[].class);
    }

    @Override
    public List<IClassType> getArguments() {
        return Collections.emptyList();
    }

    @Override
    public List<ISkriptPattern> getPatterns() {
        return Collections.emptyList();
    }

    @Override
    public Method getMethod() {
        //We have a value already
        return METHOD;
    }

    @Override
    public IClassType getReturnType() {
        return RETURN_TYPE;
    }

    public static String concatenate(ISkriptExpression... args) {
        StringBuilder builder = new StringBuilder();
        for(ISkriptExpression arg : args) {
            //Compiler replaces getValue with the actual value
            builder.append(arg.getValue());
        }
        return builder.toString();
    }
}
