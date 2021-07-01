package com.bigbade.javaskript.api.skript.code;

import com.bigbade.javaskript.api.java.util.IClassType;
import com.bigbade.javaskript.api.skript.pattern.ISkriptPattern;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Represents a single Skript instruction, allows statements or expressions.
 */
@SuppressWarnings("unused")
public interface ISkriptInstruction {
    /**
     * Returns the arguments of the instruction.
     * @return Arguments
     */
    List<IClassType> getArguments();

    /**
     * The patterns of the instruction.
     */
    List<ISkriptPattern> getPatterns();

    /**
     * Method of the instruction
     */
    Method getMethod();
}
