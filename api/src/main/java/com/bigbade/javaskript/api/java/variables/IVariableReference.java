package com.bigbade.javaskript.api.java.variables;

/**
 * A reference to a variable. Usually passed to methods or used to call methods.
 */
@SuppressWarnings("unused")
public interface IVariableReference {
    /**
     * The variable referenced by this.
     * @return Variable referenced
     */
    IVariableDef getVariable();
}
