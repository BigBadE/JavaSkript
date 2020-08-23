package software.bigbade.javaskript.api.objects.variable;

import software.bigbade.javaskript.api.variables.Type;

/**
 * A variable in the class.
 * Overridden by superclasses to represent the different types of variables (Stack, Stored, and Constants)
 * @param <T> Type of the variable, used to enforce type safety.
 */
public interface LocalVariable<T> {
    int getNumber();

    void setNumber(int number);

    Type getType();

    void setType(Type type);
}
