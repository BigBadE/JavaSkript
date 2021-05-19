package com.bigbade.javaskript.parser.variable;

import com.bigbade.javaskript.api.java.util.IClassType;
import com.bigbade.javaskript.api.skript.defs.ISkriptDef;
import com.bigbade.javaskript.api.skript.variables.ISkriptVariableDef;
import com.bigbade.javaskript.parser.exceptions.SkriptParseException;
import com.bigbade.javaskript.api.java.util.JavaClassType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class VariableManager {
    private final Map<ISkriptDef, Map<String, ISkriptVariableDef>> localVariables = new HashMap<>();
    private final Map<String, ISkriptVariableDef> globalVariables = new HashMap<>();
    private final Map<String, ISkriptVariableDef> options = new HashMap<>();

    public ISkriptVariableDef getLocalVariable(ISkriptDef parent, IClassType type, String variable) {
        ISkriptVariableDef foundVariable = localVariables.getOrDefault(parent, Collections.emptyMap()).get(variable);
        if (foundVariable == null) {
            Map<String, ISkriptVariableDef> foundMap = localVariables.getOrDefault(parent, new HashMap<>());
            foundMap.put(variable, new SkriptVariableDef(type));
            localVariables.put(parent, foundMap);
        }
        return foundVariable;
    }

    public ISkriptVariableDef getGlobalVariable(IClassType type, String variable) {
        ISkriptVariableDef foundVariable = globalVariables.get(variable);
        if (foundVariable == null) {
            globalVariables.put(variable, new SkriptVariableDef(type));
        }
        return foundVariable;
    }

    public <T> void addOption(String option, T value) {
        options.put(option, new SkriptVariableDef(new JavaClassType(value.getClass())));
    }

    public ISkriptVariableDef getOption(String variable, String line, int lineNumber) {
        ISkriptVariableDef foundVariable = options.get(variable);
        if (foundVariable == null) {
            throw new SkriptParseException(lineNumber, line, "No option found named {@" + variable + "}");
        }
        return foundVariable;
    }
}
