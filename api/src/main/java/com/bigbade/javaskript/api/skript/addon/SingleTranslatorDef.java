package com.bigbade.javaskript.api.skript.addon;

import com.bigbade.javaskript.api.java.defs.IPackageDef;
import com.bigbade.javaskript.api.java.variables.IVariableDef;
import com.bigbade.javaskript.api.skript.code.ITranslatorFactory;
import com.bigbade.javaskript.api.skript.defs.IValueTranslator;
import com.bigbade.javaskript.api.skript.pattern.ISkriptPattern;
import lombok.Getter;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public abstract class SingleTranslatorDef<T> implements ISkriptFunctionDef<T> {
    private final List<IVariableDef> variables = new ArrayList<>();

    private IValueTranslator<T> startingTranslator;
    private List<ISkriptPattern> patterns;

    @Nullable
    @Override
    public Map<String, IValueTranslator<?>> getTranslators() {
        return null;
    }

    public abstract IValueTranslator<T> getTranslator(ITranslatorFactory factory);

    public void addVariables() {
        //Can be overridden by subclasses
    }

    @Override
    public void init(List<ISkriptPattern> patterns, ITranslatorFactory factory) {
        this.patterns = patterns;
        addVariables();
        startingTranslator = getTranslator(factory);
    }

    protected void addVariable(IVariableDef variableDef) {
        variables.add(variableDef);
    }

    @Override
    public final void operate(Map<String, ?> yamlValues, int patternData, IPackageDef mainPackage) {
        //Not used by this implementation
    }
}
