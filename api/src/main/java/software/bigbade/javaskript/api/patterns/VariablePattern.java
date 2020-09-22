package software.bigbade.javaskript.api.patterns;

import lombok.Getter;
import software.bigbade.javaskript.api.variables.ClassType;
import software.bigbade.javaskript.api.variables.SkriptType;
import software.bigbade.javaskript.api.variables.SkriptTypes;

public class VariablePattern implements PatternType {
    @Getter
    private SkriptType<?> type;

    @Override
    public void parseString(String input) {
        type = SkriptTypes.getSkriptType(input).orElse(new ClassType<>(input));
    }

    @Override
    public boolean matches(String input) {
        return true;
    }

    @Override
    public int getTotalSize() {
        return -1;
    }
}
