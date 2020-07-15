package software.bigbade.javaskript.api.objects;

import lombok.Getter;
import software.bigbade.javaskript.api.variables.Type;

import javax.annotation.Nullable;

@Getter
public class LocalVariable {
    private final int number;
    private final Type type;
    @Nullable
    private final String name;

    public LocalVariable(int number, Class<?> clazz, @Nullable String name) {
        this.number = number;
        this.type = Type.getType(clazz);
        this.name = name;
    }

    public LocalVariable(int number, Type type, @Nullable String name) {
        this.number = number;
        this.type = type;
        this.name = name;
    }
}
