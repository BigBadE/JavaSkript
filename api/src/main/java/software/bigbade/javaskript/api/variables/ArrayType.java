package software.bigbade.javaskript.api.variables;

import lombok.RequiredArgsConstructor;
import software.bigbade.javaskript.api.exception.IllegalScriptException;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ArrayType<T> extends GenericType<T[]> {
    private final SkriptType<T> type;

    @Override
    public String serialize(T[] input) {
        if(!type.isSerializable()) {
            throw new IllegalScriptException("Array cannot be serialized: " + type.getName());
        }
        StringBuilder output = new StringBuilder("[");

        for(T value : input) {
            output.append(type.serialize(value)).append("\n");
        }
        return output.substring(0, output.length()-1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T[] deserialize(String input) {
        if(!type.isSerializable()) {
            throw new IllegalScriptException("Array cannot be serialized: " + type.getName());
        }

        List<T> output = new ArrayList<>();
        for(String value : input.substring(0, input.length()-1).split("\n")) {
            output.add(type.deserialize(value));
        }
        return (T[]) output.toArray();
    }

    @Override
    public boolean isSerializable() {
        return true;
    }

    @Override
    public boolean isSet() {
        return true;
    }

    @Override
    public boolean isType(String input) {
        return input.charAt(0) == '[' && input.charAt(input.length()-1) == ']';
    }

    @Override
    public String getName() {
        return "array";
    }
}
