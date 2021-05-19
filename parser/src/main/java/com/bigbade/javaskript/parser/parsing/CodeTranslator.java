package com.bigbade.javaskript.parser.parsing;

import com.bigbade.javaskript.api.skript.defs.ICodeDef;
import com.bigbade.javaskript.api.skript.defs.ICodeTranslator;

public class CodeTranslator implements ICodeTranslator {
    @Override
    public ICodeDef getValue() {
        return null;
    }

    @Override
    public void readLine(String line) {

    }

    @Override
    public boolean readsFirstLine() {
        return false;
    }
}
