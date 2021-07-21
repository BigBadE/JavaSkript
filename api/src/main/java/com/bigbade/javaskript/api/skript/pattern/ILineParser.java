package com.bigbade.javaskript.api.skript.pattern;

import com.bigbade.javaskript.api.skript.addon.IAddonManager;
import com.bigbade.javaskript.api.skript.code.IParsedInstruction;
import com.bigbade.javaskript.api.skript.code.ISkriptInstruction;

import java.util.List;

public interface ILineParser {
    /**
     * Parses a line into a parsed instruction
     *
     * @param instructions Instructions to check against
     * @param line         Line to parse
     * @param lineNumber   Line number
     * @param <T>          Type of instruction to parse
     * @param <E>          Type of output instruction
     * @return Parsed instruction
     */
    <T extends ISkriptInstruction, E extends IParsedInstruction> E getInstruction(List<T> instructions,
                                                                                  String line, int lineNumber);

    /**
     * Parses a line into a parsed instruction, using all instructions in the addon manager
     *
     * @param line       Line to parse
     * @param lineNumber Line number
     * @return Parsed instruction
     */
    IParsedInstruction getInstruction(String line, int lineNumber);

    /**
     * Parses a %variable% into an instruction to get said variable
     *
     * @param patternPart Pattern part requiring the variable
     * @param variable    Variable to check against
     * @param lineNumber  Line number
     * @return Parsed instruction returning found variable
     */
    IParsedInstruction parseVariable(IPatternPart patternPart, String variable, int lineNumber);

    /**
     * Returns the addon manager used by the line parser
     *
     * @return Addon manager
     */
    IAddonManager getAddonManager();
}
