package com.bigbade.javaskript.api.java.variables;

import com.bigbade.javaskript.api.java.util.IClassType;

/**
 * A constraint for a generic. Can be assumed in most cases.
 */
@SuppressWarnings("unused")
public interface IGenericConstraint {
    /**
     * Gets the generic identifier, usually a single character.
     * @return Generic identifier
     */
    String getIdentifier();

    /**
     * Gets the constraint of the generic, either another generic or a class.
     * @return Constraint of the generic
     */
    IClassType getConstraint();
}
