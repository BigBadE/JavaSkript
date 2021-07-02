package com.bigbade.javaskript.api.skript.code;

import com.bigbade.javaskript.api.skript.defs.ICodeDef;

/**
 * Parsed branch function
 */
public interface IParsedBranchFunction extends IParsedInstruction {
    /**
     * Gets the code in this parsed branch function
     * @return Code in the branch function
     */
    ICodeDef getCodeDef();
}
