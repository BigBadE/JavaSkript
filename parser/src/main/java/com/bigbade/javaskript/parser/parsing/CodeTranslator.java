package com.bigbade.javaskript.parser.parsing;

import com.bigbade.javaskript.api.skript.defs.ICodeDef;
import com.bigbade.javaskript.api.skript.defs.ICodeTranslator;
import com.bigbade.javaskript.api.skript.pattern.ILineParser;
import com.bigbade.javaskript.parser.impl.SkriptCodeDef;

public class CodeTranslator implements ICodeTranslator {
    private final ICodeDef codeDef = new SkriptCodeDef();

    @Override
    public ICodeDef getValue() {
        return codeDef;
    }

    @Override
    public void readLine(ILineParser lineParser, int lineNumber, String line) {
        codeDef.addInstruction(lineParser.getInstruction(line, lineNumber));
    }

    @Override
    public void startBranchFunction(ILineParser lineParser, int lineNumber, String line) {
        //TODO implement branch functions
    }

    @Override
    public void endBranchFunction(ILineParser lineParser, int lineNumber) {
        //TODO implement branch functions
    }

    @Override
    public boolean readsFirstLine() {
        return false;
    }
}
