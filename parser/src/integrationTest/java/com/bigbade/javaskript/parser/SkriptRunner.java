package com.bigbade.javaskript.parser;

import com.bigbade.javaskript.api.skript.addon.ISkriptFunctionDef;
import com.bigbade.javaskript.api.skript.defs.IParsingDef;
import com.bigbade.javaskript.parser.api.SkriptFile;
import org.junit.jupiter.api.Assertions;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkriptRunner {
    private final Map<Class<ISkriptFunctionDef>, Map<String, ?>> defClasses = new HashMap<>();
    private Class<ISkriptFunctionDef> currentDef;
    private Map<String, Object> currentArguments;

    public SkriptRunner startDefClass(Class<ISkriptFunctionDef> clazz) {
        if(currentDef != null) {
            defClasses.put(currentDef, currentArguments);
        }
        currentDef = clazz;
        currentArguments = new HashMap<>();
        return this;
    }

    public SkriptRunner addArgument(String key, Object value) {
        currentArguments.put(key, value);
        return this;
    }

    public void run(String testName, String script) {
        if(currentDef != null) {
            defClasses.put(currentDef, currentArguments);
        }
        SkriptFile file = new SkriptParser(testName).parseSkript(new StringReader(script));
        List<IParsingDef> defs = file.getParsedFunctions();
        Assertions.assertEquals(defClasses.size(), defs.size());
        int i = 0;
        for(Map.Entry<Class<ISkriptFunctionDef>, Map<String, ?>> entry : defClasses.entrySet()) {
            IParsingDef def = defs.get(i++);
            Assertions.assertEquals(entry.getKey(), def.getFunctionDef().getClass());
            for(Map.Entry<String, ?> keyValue : entry.getValue().entrySet()) {
                Assertions.assertEquals(def.getKeyValues().get(keyValue.getKey()), keyValue.getValue());
            }
        }
    }
}
