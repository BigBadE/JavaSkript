package software.bigbade.javaskript.api.variables;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Variables {
    @Getter
    private final List<SkriptType<?>> allVariables = new ArrayList<>();

    public void addVariable(SkriptType<?> variable) {
        allVariables.add(variable);
    }
}
