package software.bigbade.javaskript.api.variables;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public final class Type {
    public static final Type VOID_TYPE = new Type(0, null, 1443168256, 1);
    public static final Type BOOLEAN_TYPE = new Type(1, null, 1509950721, 1);
    public static final Type CHAR_TYPE = new Type(2, null, 1124075009, 1);
    public static final Type BYTE_TYPE = new Type(3, null, 1107297537, 1);
    public static final Type SHORT_TYPE = new Type(4, null, 1392510721, 1);
    public static final Type INT_TYPE = new Type(5, null, 1224736769, 1);
    public static final Type FLOAT_TYPE = new Type(6, null, 1174536705, 1);
    public static final Type LONG_TYPE = new Type(7, null, 1241579778, 1);
    public static final Type DOUBLE_TYPE = new Type(8, null, 1141048066, 1);
    private final int sort;
    private final char[] buf;
    private final int off;
    private final int len;

    private Type(int var1, char[] var2, int var3, int var4) {
        this.sort = var1;
        this.buf = var2;
        this.off = var3;
        this.len = var4;
    }

    public static Type getType(String name) {
        return getType(name.toCharArray(), 0);
    }

    public static Type getType(Class<?> var0) {
        if (var0.isPrimitive()) {
            if (var0 == Integer.TYPE) {
                return INT_TYPE;
            } else if (var0 == Void.TYPE) {
                return VOID_TYPE;
            } else if (var0 == Boolean.TYPE) {
                return BOOLEAN_TYPE;
            } else if (var0 == Byte.TYPE) {
                return BYTE_TYPE;
            } else if (var0 == Character.TYPE) {
                return CHAR_TYPE;
            } else if (var0 == Short.TYPE) {
                return SHORT_TYPE;
            } else if (var0 == Double.TYPE) {
                return DOUBLE_TYPE;
            } else {
                return var0 == Float.TYPE ? FLOAT_TYPE : LONG_TYPE;
            }
        } else {
            return getType(getDescriptor(var0));
        }
    }

    private static Type getType(char[] var0, int var1) {
        int var2;
        switch (var0[var1]) {
            case 'B':
                return BYTE_TYPE;
            case 'C':
                return CHAR_TYPE;
            case 'D':
                return DOUBLE_TYPE;
            case 'E':
            case 'G':
            case 'H':
            case 'K':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'T':
            case 'U':
            case 'W':
            case 'X':
            case 'Y':
            case 'F':
                return FLOAT_TYPE;
            case 'I':
                return INT_TYPE;
            case 'J':
                return LONG_TYPE;
            case 'L':
                for (var2 = 1; var0[var1 + var2] != ';'; ++var2) {

                }
                return new Type(10, var0, var1 + 1, var2 - 1);
            case 'S':
                return SHORT_TYPE;
            case 'V':
                return VOID_TYPE;
            case 'Z':
                return BOOLEAN_TYPE;
            case '[':
                for (var2 = 1; var0[var1 + var2] == '['; ++var2) {

                }

                if (var0[var1 + var2] == 'L') {
                    ++var2;

                    while (var0[var1 + var2] != ';') {
                        ++var2;
                    }
                }

                return new Type(9, var0, var1, var2 + 1);
            default:
                return new Type(11, var0, var1, var0.length - var1);
        }
    }

    public static String getMethodDescriptor(Type type, Type... types) {
        StringBuilder builder = new StringBuilder();
        builder.append('(');

        for (Type paramType : types) {
            paramType.getDescriptor(builder);
        }

        builder.append(')');
        type.getDescriptor(builder);
        return builder.toString();
    }

    public static String getDescriptor(Class<?> clazz) {
        StringBuilder builder = new StringBuilder();
        getDescriptor(builder, clazz);
        return builder.toString();
    }

    public static String getConstructorDescriptor(Constructor<?> constructor) {
        Class<?>[] params = constructor.getParameterTypes();
        StringBuilder builder = new StringBuilder();
        builder.append('(');

        for (Class<?> param : params) {
            getDescriptor(builder, param);
        }

        return builder.append(")V").toString();
    }

    public static String getMethodDescriptor(Method method) {
        Class<?>[] params = method.getParameterTypes();
        StringBuilder builder = new StringBuilder();
        builder.append('(');

        for (Class<?> param : params) {
            getDescriptor(builder, param);
        }

        builder.append(')');
        getDescriptor(builder, method.getReturnType());
        return builder.toString();
    }

    private static void getDescriptor(StringBuilder builder, Class<?> clazz) {
        Class<?> tempClass;
        for (tempClass = clazz; !tempClass.isPrimitive(); tempClass = tempClass.getComponentType()) {
            if (!tempClass.isArray()) {
                builder.append('L');
                String className = tempClass.getName();
                int var4 = className.length();

                for (int i = 0; i < var4; ++i) {
                    char nameLetter = className.charAt(i);
                    builder.append(nameLetter == '.' ? '/' : nameLetter);
                }

                builder.append(';');
                return;
            }

            builder.append('[');
        }

        builder.append(Type.getTypeChar(tempClass));
    }

    public static char getTypeChar(Class<?> clazz) {
        if (clazz == Integer.TYPE) {
            return 'I';
        } else if (clazz == Void.TYPE) {
            return 'V';
        } else if (clazz == Boolean.TYPE) {
            return 'Z';
        } else if (clazz == Byte.TYPE) {
            return 'B';
        } else if (clazz == Character.TYPE) {
            return 'C';
        } else if (clazz == Short.TYPE) {
            return 'S';
        } else if (clazz == Double.TYPE) {
            return 'D';
        } else if (clazz == Float.TYPE) {
            return 'F';
        } else {
            return 'J';
        }
    }

    public int getSort() {
        return this.sort;
    }

    public int getDimensions() {
        int var1;
        for (var1 = 1; this.buf[this.off + var1] == '['; ++var1) {

        }

        return var1;
    }

    public Type getElementType() {
        return getType(this.buf, this.off + this.getDimensions());
    }

    public String getClassName() {
        switch (this.sort) {
            case 0:
                return "void";
            case 1:
                return "boolean";
            case 2:
                return "char";
            case 3:
                return "byte";
            case 4:
                return "short";
            case 5:
                return "int";
            case 6:
                return "float";
            case 7:
                return "long";
            case 8:
                return "double";
            case 9:
                StringBuilder var1 = new StringBuilder(this.getElementType().getClassName());
                for (int var2 = this.getDimensions(); var2 > 0; --var2) {
                    var1.append("[]");
                }
                return var1.toString();
            case 10:
                return (new String(this.buf, this.off, this.len)).replace('/', '.');
            default:
                return null;
        }
    }

    public String getInternalName() {
        return new String(this.buf, this.off, this.len);
    }

    public String getDescriptor() {
        StringBuilder builder = new StringBuilder();
        this.getDescriptor(builder);
        return builder.toString();
    }

    public void getDescriptor(StringBuilder var1) {
        if (this.buf == null) {
            var1.append((char) ((this.off & -16777216) >>> 24));
        } else if (this.sort == 10) {
            var1.append('L');
            var1.append(this.buf, this.off, this.len);
            var1.append(';');
        } else {
            var1.append(this.buf, this.off, this.len);
        }
    }

    public int getSize() {
        return this.buf == null ? this.off & 255 : 1;
    }

    public int getOpcode(int opcode) {
        if(opcode != 46 && opcode != 79) {
            if(buf == null) {
                return opcode + ((off & 16711680) >> 16);
            } else {
                return opcode + 4;
            }
        } else {
            if(buf == null) {
                return opcode + ((off & '\uff00') >> 8);
            } else {
                return opcode + 4;
            }
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof Type)) {
            return false;
        } else {
            Type type = (Type) object;
            if (this.sort != type.sort) {
                return false;
            } else {
                if (this.sort >= 9) {
                    if (this.len != type.len) {
                        return false;
                    }

                    int offset = this.off;
                    int typeOffset = type.off;

                    for (int i = offset + this.len; offset < i; ++typeOffset) {
                        if (this.buf[offset] != type.buf[typeOffset]) {
                            return false;
                        }

                        offset = typeOffset;
                    }
                }

                return true;
            }
        }
    }

    public int hashCode() {
        int var1 = 13 * this.sort;
        if (this.sort >= 9) {
            int var2 = this.off;

            for (int var3 = var2 + this.len; var2 < var3; ++var2) {
                var1 = 17 * (var1 + this.buf[var2]);
            }
        }

        return var1;
    }

    public String toString() {
        return this.getDescriptor();
    }
}
