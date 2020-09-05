package software.bigbade.javaskript.api.variables;

import lombok.Getter;

public abstract class GenericType<T> implements SkriptType<T> {
    @Getter
    private final Type type = getName() == null ? null : Type.getType("L" + getName().replace(".", "/") + ";");

    public T deserialize(String input) {
        if(isSerializable()) {
            throw new IllegalStateException("Unimplemented deserialize call at " + getClass());
        } else {
            throw new IllegalStateException("Cannot call deserialize on non-serializable object!");
        }
    }

    public String serialize(T input) {
        if(isSerializable()) {
            throw new IllegalStateException("Unimplemented serialize call at " + getClass());
        } else {
            throw new IllegalStateException("Cannot call serialize on non-serializable object!");
        }
    }

    public T getObject(String input) {
        if(isSet()) {
            throw new IllegalStateException("Unimplemented getObject call at " + getClass());
        } else {
            throw new IllegalStateException("Cannot call getObject on non-set object!");
        }
    }
}
