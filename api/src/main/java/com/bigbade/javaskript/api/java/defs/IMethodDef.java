package com.bigbade.javaskript.api.java.defs;

import com.bigbade.javaskript.api.java.util.IClassType;
import com.bigbade.javaskript.api.java.util.Modifiers;
import com.bigbade.javaskript.api.java.variables.IGenericConstraint;
import com.bigbade.javaskript.api.java.variables.IVariableDef;

import java.util.List;
import java.util.Optional;

/**
 * A definition for a method. Methods consist of
 */
@SuppressWarnings("unused")
public interface IMethodDef {
    /**
     * Returns the name of a method. Constructors will always be named &gt;init&lt;
     * @return Name of the method
     */
    String getMethodName();

    /**
     * Gets the return type of the method. Can be a generic type also.
     * @return Return type of this method
     */
    IClassType getReturnType();

    /**
     * Returns the modifiers of the constructor. Allows public, protected, private, abstract, static, final,
     * synchronized, native, and strict.
     * @return Modifiers of the constructor
     */
    Modifiers getModifiers();

    /**
     * Sets the modifiers of the constructor. Allows public, protected, private, abstract, static, final,
     * synchronized, native, and strict.
     * @param modifiers Modifiers of the constructor
     */
    void setModifiers(Modifiers modifiers);

    /**
     * Sets the code of the method.
     * @param code Code block to add to the method
     */
    void setCodeBlock(ICodeDef code);

    /**
     * Gets the code block of the method.
     * @return The code of the method block, or nothing if the method is part of an interface
     */
    Optional<ICodeDef> getCodeBlock();

    /**
     * Adds a generic with the constraint to the method.
     * @param constraint Generic to add
     */
    void addGenericConstraint(IGenericConstraint constraint);

    /**
     * Gets a list of all generics with their constraints.
     * @return List of constraints, empty if none
     */
    List<IGenericConstraint> getConstraints();

    /**
     * Adds a parameter to the method. Parameter modifiers are more strict the variable modifiers!
     * Only allows the final modifier.
     * @param variable Variable to add to the method
     * @throws IllegalArgumentException if the parameter has illegal modifiers
     */
    void addParameter(IVariableDef variable);

    /**
     * Adds parameters to the method. Parameter modifiers are more strict the variable modifiers!
     * Only allows the final modifier.
     * @param variables Variables to add to the method
     * @throws IllegalArgumentException if the parameter has illegal modifiers
     */
    void addParameters(List<IVariableDef> variables);

    /**
     * Gets all parameters of the method.
     * @return List of parameters, empty if none
     */
    List<IVariableDef> getParameters();
}
