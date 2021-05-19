package com.bigbade.javaskript.parser.parsing;

import com.bigbade.javaskript.api.skript.code.ITranslatorFactory;
import com.bigbade.javaskript.api.skript.defs.ICodeTranslator;

public class TranslatorFactory implements ITranslatorFactory {
    @Override
    public ICodeTranslator getCodeTranslator() {
        return new CodeTranslator();
    }
}
