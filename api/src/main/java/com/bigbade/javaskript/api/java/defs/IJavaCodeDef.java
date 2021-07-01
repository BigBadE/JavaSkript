package com.bigbade.javaskript.api.java.defs;

import com.bigbade.javaskript.api.java.code.statements.IStatement;

import java.util.List;

/**
 * Defines a block of code, in Java all blocks are surrounded by curly brackets.
 */
@SuppressWarnings("unused")
public interface IJavaCodeDef {
    /**
     * Adds the statement to the block of code.
     * @param statement Statement to add
     */
    void addStatement(IStatement statement);

    /**
     * Gets the statements in the code block.
     * @return Statements in this code block
     */
    List<IStatement> getStatements();
}
