package software.bigbade.javaskript.api.variables;

public interface SkriptType<T> {
    T deserialize(String input);

    String serialize();

    boolean isSerializable();

    /**
     * gets if the type is set
     * @return If the type has preset values, such as entity names
     */
    boolean isSet();

    /**
     * Only used for set types, returns the type given the name
     * @param input The name
     * @return The type
     */
    T getType(String input);

    boolean isType(String input);

    Class<T> getTypeClass();
}
