package software.bigbade.javaskript.api.variables;

import lombok.Getter;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public final class SkriptTypes {
    private SkriptTypes() {}

    public static void registerSkriptType(SkriptType type) {
        SKRIPT_TYPES.add(type);
    }

    @Nullable
    public static SkriptType getSkriptType(String type) {
        for(SkriptType skriptType : SKRIPT_TYPES) {
            if(skriptType.getName().equals(type)) {
                return skriptType;
            }
        }
        return null;
    }

    public static final SkriptType VOID = new PrimitiveType() {
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
            return null;
        }
    };

    public static final SkriptType STRING = new GenericType<String>() {
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
            return String.class.getName();
        }
    };

    public static final SkriptType INTEGER = new PrimitiveType() {
        public int deserialize(String input) {
            return Integer.parseInt(input);
        }

        public String serialize(int input) {
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

        public Type getType() {
            return Type.INT_TYPE;
        }

        public int getType(String input) {
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

    @Getter
    private static final List<SkriptType> SKRIPT_TYPES = Arrays.asList(VOID, STRING, INTEGER);
}
