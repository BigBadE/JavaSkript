package software.bigbade.javaskript.api.variables;

import lombok.Getter;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.Map;

public class Variables {
    @Getter
    private final Map<String, SkriptType<?>> allVariables = new LinkedHashMap<>();

    public void addVariable(String name, SkriptType<?> variable) {
        allVariables.put(name, variable);
    }

    public void addVariables(@Nonnull Variables variables) {
        allVariables.putAll(variables.getAllVariables());
    }

}
