package com.bigbade.javaskript.parser.parsing;

import com.bigbade.javaskript.api.skript.code.IParsedBranchFunction;
import com.bigbade.javaskript.api.skript.defs.ICodeDef;
import com.bigbade.javaskript.api.skript.defs.ICodeTranslator;
import com.bigbade.javaskript.api.skript.pattern.ILineParser;
import com.bigbade.javaskript.parser.exceptions.SkriptParseException;
import com.bigbade.javaskript.parser.impl.SkriptCodeDef;

import java.util.ArrayDeque;
import java.util.Deque;

public class CodeTranslator implements ICodeTranslator {
    private final Deque<IParsedBranchFunction> branches = new ArrayDeque<>();
    private final ICodeDef codeDef = new SkriptCodeDef();

    @Override
    public ICodeDef getValue() {
        return codeDef;
    }

    @Override
    public void readLine(ILineParser lineParser, int lineNumber, String line) {
        ICodeDef target = codeDef;
        if(branches.peek() != null) {
            target = branches.peek().getCodeDef();
        }
        target.addInstruction(lineParser.getInstruction(line, lineNumber));
    }

    @Override
    public void startBranchFunction(ILineParser lineParser, int lineNumber, String line) {
        branches.push(lineParser.getInstruction(lineParser.getAddonManager().getBranchFunctionDefs(), line, lineNumber));
    }

    @Override
    public void endBranchFunction(ILineParser lineParser, int lineNumber) {
        if(branches.isEmpty()) {
            throw new SkriptParseException(lineNumber,
                    "unknown", "Tried to end branch function with empty branches deque! This is a bug, report it!");
        }
        IParsedBranchFunction branchFunction = branches.pop();
        codeDef.addInstruction(branchFunction);
    }

    @Override
    public boolean readsFirstLine() {
        return false;
    }
}
