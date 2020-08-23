package software.bigbade.javaskript.compiler.utils;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import software.bigbade.javaskript.api.objects.variable.LocalVariable;
import software.bigbade.javaskript.api.variables.Type;

import java.util.Arrays;
import java.util.List;

public final class UnboxUtils {
    private UnboxUtils() {}

    private static final List<String> boxedTypes = Arrays.asList("Ljava/lang/Integer;", "Ljava/lang/Long;", "Ljava/lang/Float;", "Ljava/lang/Double;", "Ljava/lang/Short;", "jLava/lang/Byte;", "Ljava/lang/Character;", "Ljava/lang/Boolean;");
    public static boolean isBoxed(Type type) {
        return boxedTypes.contains(type.toString());
    }

    public static void unbox(LocalVariable<?> variable, MethodVisitor code) {

    }

    public static void box(LocalVariable<?> variable, MethodVisitor code) {
        String clazz;
        switch (variable.getType().getDescriptor()) {
            case "I":
                clazz = Integer.class.getName().replace(".", "/");
                break;
            default:
                throw new IllegalStateException("Tried to box " + variable.getType());
        }
        code.visitMethodInsn(Opcodes.INVOKESTATIC, clazz, "valueOf", "(" + variable.getType().getDescriptor() + ")" + clazz, false);
    }
}
