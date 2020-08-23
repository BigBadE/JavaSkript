package software.bigbade.javaskript.api.variables;

import lombok.Getter;

public class ClassType<T> extends GenericType<T> {
    private final String name;
    @Getter
    private final Type type;

    public ClassType(String name) {
        this.name = name;
        type = Type.getType("L" + getName().replace(".", "/") + ";");
    }

    @Override
    T deserialize(String input) {
        return null;
    }

    @Override
    String serialize(T input) {
        return null;
    }

    @Override
    T getObject(String input) {
        return null;
    }

    @Override
    public boolean isSerializable() {
        return false;
    }

    @Override
    public boolean isSet() {
        return false;
    }

    @Override
    public boolean isType(String input) {
        return false;
    }

    @Override
    public String getName() {
        return name;
    }
}
