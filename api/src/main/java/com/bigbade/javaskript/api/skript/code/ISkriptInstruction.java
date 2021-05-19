package com.bigbade.javaskript.api.skript.code;

import com.bigbade.javaskript.api.java.util.IClassType;
import com.bigbade.javaskript.api.skript.pattern.ISkriptPattern;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

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
    Map<ISkriptPattern, Integer> getPatterns();

    /**
     * Method of the instruction
     */
    Method getMethod();
}
