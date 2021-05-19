package com.bigbade.javaskript.api.skript.addon;

import com.bigbade.javaskript.api.java.defs.IPackageDef;
import com.bigbade.javaskript.api.skript.code.ITranslatorFactory;
import com.bigbade.javaskript.api.skript.defs.IValueTranslator;
import com.bigbade.javaskript.api.skript.pattern.ISkriptPattern;
import lombok.Getter;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

@Getter
public abstract class SingleTranslatorDef<T> implements ISkriptFunctionDef<T> {
    private IValueTranslator<T> startingTranslator;
    private List<ISkriptPattern> patterns;

    @Nullable
    @Override
    public Map<String, IValueTranslator<?>> getTranslators() {
        return null;
    }

    public abstract IValueTranslator<T> getTranslator(ITranslatorFactory factory);

    @Override
    public void init(List<ISkriptPattern> patterns, ITranslatorFactory factory) {
        this.patterns = patterns;
        startingTranslator = getTranslator(factory);
    }

    @Override
    public final void operate(Map<String, ?> yamlValues, int patternData, IPackageDef mainPackage) {
        //Not used by this implementation
    }
}
