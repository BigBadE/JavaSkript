package com.bigbade.javaskript.api.skript.code;

import com.bigbade.javaskript.api.java.util.IClassType;

import javax.annotation.Nullable;

/**
 * Represents a basic Skript expression
 */
@SuppressWarnings("unused")
public interface ISkriptExpression extends ISkriptInstruction {
    /**
     * Gets the return type of the expression.
     * @return Return type
     */
    IClassType getReturnType();

    /**
     * If the expression can be pre-computed (like math), just compile the value.
     * @return Pre-computed value, if any
     */
    @Nullable
    default Object getValue() {
        return null;
    }
}
