package software.bigbade.javaskript.compiler.instructions;

import lombok.Getter;
import software.bigbade.javaskript.compiler.utils.Type;

@Getter
public class LocalVariable {
    private final int number;
    private final Type type;

    public LocalVariable(int number, Class<?> clazz) {
        this.number = number;
        this.type = Type.getType(clazz);
    }

    public LocalVariable(int number, Type type) {
        this.number = number;
        this.type = type;
    }
}
