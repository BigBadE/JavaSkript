package software.bigbade.javaskript.api.variables;

public interface SkriptType {
    boolean isSerializable();

    /**
     * gets if the type is set
     * @return If the type has preset values, such as entity names
     */
    boolean isSet();

    boolean isType(String input);

    Type getType();

    String getName();
}
