package com.bigbade.javaskript.translator.impl;

import com.bigbade.javaskript.api.java.defs.ClassMembers;
import com.bigbade.javaskript.api.java.util.IClassType;
import com.bigbade.javaskript.api.java.util.Modifiers;
import com.bigbade.javaskript.api.java.variables.IVariableDef;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Modifier;

@Getter
@RequiredArgsConstructor
public class JavaVariableDef implements IVariableDef {
    private final IClassType type;
    private final String identifier;
    private final VariableType variableType;

    private Modifiers modifiers;

    /**
     * Different variables have different modifier requirements
     * @param modifiers Modifiers of the variable
     */
    public void setModifiers(Modifiers modifiers) {
        switch (variableType) {
            case FIELD -> {
                if (!modifiers.isLegal(ClassMembers.FIELDS)) {
                    throw new IllegalStateException("Illegal modifiers for field: " + modifiers.getMask());
                }
                this.modifiers = modifiers;
            }
            case PARAMETER, LOCAL_VARIABLE -> {
                if ((Modifiers.PARAMETER_MODIFIERS | modifiers.getMask()) != Modifiers.PARAMETER_MODIFIERS) {
                    throw new IllegalStateException("Illegal modifiers for " + variableType + ": " + modifiers.getMask());
                }
                this.modifiers = modifiers;
            }
            default -> throw new IllegalStateException("Unknown variable type: " + variableType);
        }
    }

    /**
     * Different variable types: Field, Local variable, and parameters.
     * "this" is considered a parameter for all intents and purposes.
     */
    public enum VariableType {
        FIELD,
        LOCAL_VARIABLE,
        PARAMETER
    }
}
