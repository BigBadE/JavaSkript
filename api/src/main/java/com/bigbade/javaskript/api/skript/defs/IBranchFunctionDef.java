package com.bigbade.javaskript.api.skript.defs;

import com.bigbade.javaskript.api.skript.addon.ISkriptFunctionDef;
import com.bigbade.javaskript.api.skript.code.ISkriptInstruction;

import javax.annotation.Nullable;

public interface IBranchFunctionDef extends ISkriptInstruction {
    /**
     * Verifies that the branch function is valid
     * @return If the usage of the branch function is valid
     */
    boolean verify(@Nullable IBranchFunctionDef parent, ISkriptFunctionDef function, ICodeDef codeDef);
}
