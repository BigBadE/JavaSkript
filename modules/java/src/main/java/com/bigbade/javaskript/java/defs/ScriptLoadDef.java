package com.bigbade.javaskript.java.defs;

import com.bigbade.javaskript.api.java.defs.IPackageDef;
import com.bigbade.javaskript.api.skript.addon.SingleTranslatorDef;
import com.bigbade.javaskript.api.skript.code.ITranslatorFactory;
import com.bigbade.javaskript.api.skript.defs.ICodeDef;
import com.bigbade.javaskript.api.skript.defs.IValueTranslator;

public class ScriptLoadDef extends SingleTranslatorDef<ICodeDef> {
    @Override
    public void operate(ICodeDef startingValue, int patternData, IPackageDef mainPackage) {

    }

    @Override
    public IValueTranslator<ICodeDef> getTranslator(ITranslatorFactory factory) {
        return factory.getCodeTranslator();
    }
}
