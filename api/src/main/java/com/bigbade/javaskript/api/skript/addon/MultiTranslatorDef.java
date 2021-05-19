package com.bigbade.javaskript.api.skript.addon;

import com.bigbade.javaskript.api.java.defs.IPackageDef;
import com.bigbade.javaskript.api.skript.code.ITranslatorFactory;
import com.bigbade.javaskript.api.skript.defs.IValueTranslator;
import com.bigbade.javaskript.api.skript.pattern.ISkriptPattern;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public abstract class MultiTranslatorDef implements ISkriptFunctionDef<Void> {
    private Map<String, IValueTranslator<?>> translators = new HashMap<>();
    private List<ISkriptPattern> patterns;

    public final IValueTranslator<?> getStartingTranslator() {
        return null;
    }

    @Override
    public void init(List<ISkriptPattern> patterns, ITranslatorFactory factory) {
        this.patterns = patterns;
    }

    protected void addTranslator(String key, IValueTranslator<?> translator) {
        if (translators == null) {
            translators = new HashMap<>();
        }
        translators.put(key, translator);
    }

    @Override
    public void operate(Void startingValue, int patternData, IPackageDef mainPackage) {
        //Not used by multi-translator defs.
    }
}
