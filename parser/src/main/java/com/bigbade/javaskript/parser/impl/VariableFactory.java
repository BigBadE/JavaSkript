package com.bigbade.javaskript.parser.impl;

import com.bigbade.javaskript.api.java.util.IClassType;
import com.bigbade.javaskript.api.java.variables.IVariableDef;
import com.bigbade.javaskript.api.skript.code.IVariableFactory;
import com.bigbade.javaskript.parser.util.JavaClassType;
import com.bigbade.javaskript.translator.impl.JavaVariableDef;

public class VariableFactory implements IVariableFactory {
    @Override
    public IClassType getClassType(Class<?> clazz) {
        return new JavaClassType(clazz);
    }

    @Override
    public IVariableDef createParameter(String name, IClassType type) {
        return new JavaVariableDef(type, name, JavaVariableDef.VariableType.PARAMETER);
    }

    @Override
    public IVariableDef createLocalVariable(String name, IClassType type) {
        return new JavaVariableDef(type, name, JavaVariableDef.VariableType.LOCAL_VARIABLE);
    }

    @Override
    public IVariableDef createField(String name, IClassType type) {
        return new JavaVariableDef(type, name, JavaVariableDef.VariableType.FIELD);
    }
}
