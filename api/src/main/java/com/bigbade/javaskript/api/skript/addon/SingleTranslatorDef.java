package com.bigbade.javaskript.api.skript.addon;

import com.bigbade.javaskript.api.java.defs.IMethodDef;
import com.bigbade.javaskript.api.java.defs.IPackageDef;
import com.bigbade.javaskript.api.java.variables.IVariableDef;
import com.bigbade.javaskript.api.skript.code.ITranslatorFactory;
import com.bigbade.javaskript.api.skript.defs.IValueTranslator;
import com.bigbade.javaskript.api.skript.pattern.ISkriptPattern;
import lombok.Getter;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
public abstract class SingleTranslatorDef<T> implements ISkriptFunctionDef {
    private final List<IVariableDef> variables = new ArrayList<>();

    private IValueTranslator<T> startingTranslator;
    private List<ISkriptPattern> patterns;

    @Override
    public Map<String, IValueTranslator<?>> getTranslators() {
        return Collections.emptyMap();
    }

    public abstract IValueTranslator<T> getTranslator(ITranslatorFactory factory);

    @Override
    public void init(List<ISkriptPattern> patterns, ITranslatorFactory factory) {
        this.patterns = patterns;
        startingTranslator = getTranslator(factory);
    }

    protected void addVariable(IVariableDef variableDef) {
        variables.add(variableDef);
    }

    @Override
    public <E> E getVariable(String identifier) {
        throw new IllegalStateException("This should never be called, read the javadocs!");
    }

    @Override
    public void execute(String key) {
        throw new IllegalStateException("This should never be called, read the javadocs!");
    }

    @Override
    public final void operate(Map<String, ?> yamlValues, int patternData) {
        //Not used by this implementation
        throw new IllegalStateException("Called operate with a map on a single translator");
    }

    @Override
    public final IMethodDef locate(Map<String, ?> yamlValues, int patternData, IPackageDef mainPackage) {
        //Not used by this implementation
        throw new IllegalStateException("Called locate with a map on a single translator");
    }

    @Override
    public final void operate(Object value, int patternData) {
        try {
            //noinspection unchecked
            operateOnDef((T) value, patternData);
        } catch (ClassCastException e) {
            throw new IllegalStateException("Translator gave a " + value.getClass() + " but expected something else!", e);
        }
    }

    protected abstract void operateOnDef(T value, int patternData);
}
