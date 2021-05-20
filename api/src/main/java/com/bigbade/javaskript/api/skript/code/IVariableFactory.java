package com.bigbade.javaskript.api.skript.code;

import com.bigbade.javaskript.api.java.util.IClassType;
import com.bigbade.javaskript.api.java.variables.IVariableDef;

public interface IVariableFactory {
    IClassType getClassType(Class<?> clazz);

    IVariableDef createParameter(String name, IClassType type);

    IVariableDef createLocalVariable(String name, IClassType type);

    IVariableDef createField(String name, IClassType type);
}
