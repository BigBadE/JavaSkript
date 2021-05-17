package com.bigbade.javaskript.api.java.variables;

import com.bigbade.javaskript.api.java.util.IClassType;
import com.bigbade.javaskript.api.java.util.Modifiers;

/**
 * Defines a variable. Unlike a reference, this is used to declare a variable. Usually used in method defs, class defs,
 * or as parameters for a method.
 */
@SuppressWarnings("unused")
public interface IVariableDef {
    /**
     * Gets the variable type.
     * @return Type of the variable
     */
    IClassType getType();

    /**
     * Gets the modifiers of the variable. Allows public, protected, private, static, final, transient, and volatile.
     * @return Modifiers of the variable
     */
    Modifiers getModifiers();

    /**
     * Gets the identifier of the variable, a.k.a. the name.
     * @return Identifier of the variable
     */
    String getIdentifier();
}
