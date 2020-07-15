package software.bigbade.javaskript.api.variables;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class Variables {
    @Getter
    private final Map<String, SkriptType<?>> allVariables = new HashMap<>();

    public void addVariable(String name, SkriptType<?> variable) {
        allVariables.put(name, variable);
    }
}
