package software.bigbade.javaskript.api.variables;

import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class SkriptTypes {
    private SkriptTypes() {}

    public static void registerSkriptType(SkriptType<?> type) {
        SKRIPT_TYPES.add(type);
    }

    public static void registerSkriptTypes(SkriptType<?>... types) {
        SKRIPT_TYPES.addAll(Arrays.asList(types));
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<SkriptType<T>> getSkriptType(String type) {
        for(SkriptType<?> skriptType : SKRIPT_TYPES) {
            if(skriptType.getName().equals(type)) {
                return Optional.of((SkriptType<T>) skriptType);
            }
        }
        return Optional.empty();
    }

    public static final SkriptType<Void> VOID = new GenericType<Void>() {
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
        public Type getType() {
            return Type.VOID_TYPE;
        }

        @Override
        public String getName() {
            return "void";
        }
    };

    public static final SkriptType<String> STRING = new GenericType<String>() {
        @Override
        public String deserialize(String input) {
            return input;
        }

        @Override
        public String serialize(String input) {
            return input;
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
        public String getObject(String input) {
            return input;
        }

        @Override
        public boolean isType(String input) {
            return true;
        }

        @Override
        public String getName() {
            return "string";
        }
    };

    public static final SkriptType<Integer> INTEGER = new GenericType<Integer>() {
        @Override
        public Integer deserialize(String input) {
            return getObject(input);
        }

        @Override
        public String serialize(Integer input) {
            return input + "";
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
        public Type getType() {
            return Type.INT_TYPE;
        }

        @Override
        public Integer getObject(String input) {
            return Integer.parseInt(input);
        }

        @Override
        public boolean isType(String input) {
            try {
                Integer.parseInt(input);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        @Override
        public String getName() {
            return "int";
        }
    };

    public static final SkriptType<Boolean> BOOLEAN = new GenericType<Boolean>() {
        @Override
        public Boolean deserialize(String input) {
            return getObject(input);
        }

        @Override
        public String serialize(Boolean input) {
            return input + "";
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
        public Type getType() {
            return Type.BOOLEAN_TYPE;
        }

        @Override
        public Boolean getObject(String input) {
            return Boolean.parseBoolean(input);
        }

        @Override
        public boolean isType(String input) {
            return input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false");
        }

        @Override
        public String getName() {
            return "boolean";
        }
    };

    @SneakyThrows
    private static List<SkriptType<?>> getFields() {
        List<SkriptType<?>> fields = new ArrayList<>();
        for(Field field : SkriptTypes.class.getFields()) {
            fields.add((SkriptType<?>) field.get(null));
        }
        return fields;
    }

    private static final List<SkriptType<?>> SKRIPT_TYPES = getFields();
}
