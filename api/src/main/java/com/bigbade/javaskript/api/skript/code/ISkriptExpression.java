package com.bigbade.javaskript.api.skript.code;

import com.bigbade.javaskript.api.java.util.IClassType;

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
}
