package com.bigbade.javaskript.api.skript.defs;

import com.bigbade.javaskript.api.skript.code.IParsedInstruction;

import java.util.List;

/**
 * Represents a block of Skript code in memory.
 */
@SuppressWarnings("unused")
public interface ICodeDef {
    /**
     * Gets all the Skript instructions in this block
     * @return Instructions list
     */
    List<IParsedInstruction> getInstructions();

    /**
     * Adds an instruction to this block
     * @param instruction Instruction to add
     */
    void addInstruction(IParsedInstruction instruction);

    /**
     * !! only allowed to be called in methods with javadocs marking so. Any other calls will error !!
     *
     * A method replaced by the compiler with the instructions in the code block.
     */
    void execute();
}
