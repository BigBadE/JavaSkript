package software.bigbade.javaskript.api.variables;

import lombok.Getter;

public abstract class GenericType<T> implements SkriptType {
    @Getter
    private final Type type = getName() == null ? null : Type.getType("L" + getName().replace(".", "/") + ";");

    abstract T deserialize(String input);

    abstract String serialize(T input);

    abstract T getObject(String input);
}
