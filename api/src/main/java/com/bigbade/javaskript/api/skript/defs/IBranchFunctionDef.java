package com.bigbade.javaskript.api.skript.defs;

import com.bigbade.javaskript.api.java.variables.IVariableDef;
import com.bigbade.javaskript.api.skript.addon.ISkriptFunctionDef;
import com.bigbade.javaskript.api.skript.code.IVariableFactory;

import javax.annotation.Nullable;
import java.util.List;

/**
 * An effect that causes a change in the control flow of the method.
 * Examples are if or for loops.
 */
public interface IBranchFunctionDef {
    /**
     * Verifies that the branch function is valid
     * @return If the usage of the branch function is valid
     */
    boolean verify(@Nullable IBranchFunctionDef parent, ISkriptFunctionDef function, ICodeDef codeDef);

    /**
     * Gets the variables of the def
     * @return Def's variables
     */
    List<IVariableDef> getVariables();

    /**
     * Sets up the variables of the def
     * @param factory Variable factory
     */
    void setupVariables(IVariableFactory factory);
}
