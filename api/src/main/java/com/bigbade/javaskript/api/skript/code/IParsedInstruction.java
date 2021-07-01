package com.bigbade.javaskript.api.skript.code;

import java.util.List;

/**
 * Represents an instruction that has been parsed and has attached data and arguments.
 */
public interface IParsedInstruction {
    /**
     * Gets the instruction.
     * @return Instruction represented by this parsed instruction
     */
    ISkriptInstruction getInstruction();

    /**
     * Gets the arguments of the instruction.
     * @return Parsed arguments of the instruction
     */
    List<IParsedInstruction> getParsedArguments();

    /**
     * Gets the data associated with the pattern.
     * @return Pattern data
     */
    Object getPatternData();

}
