package software.bigbade.javaskript.api.variables;

public interface SkriptType<T> {
    boolean isSerializable();

    String serialize(T input);

    T deserialize(String input);

    /**
     * gets if the type is set
     * @return If the type has preset values, such as entity names
     */
    boolean isSet();

    T getObject(String input);

    boolean isType(String input);

    Type getType();

    String getName();
}
