package software.bigbade.javaskript.compiler.utils;

import lombok.SneakyThrows;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import software.bigbade.javaskript.api.variables.Type;
import software.bigbade.javaskript.compiler.java.BasicJavaClass;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

public class MethodRemapper extends MethodVisitor {
    private static final ClassVisitor classVisitor = ((BasicJavaClass) UtilsClass.getUtilsMethodBuilder())
            .getClassBuilder();
    private static final Set<Method> alreadyRemapped = new HashSet<>();
    private final Set<String> writtenFields = new HashSet<>();
    private final String original;

    public MethodRemapper(Method originalMethod) {
        super(Opcodes.ASM9, classVisitor.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
                originalMethod.getName(), Type.getMethodDescriptor(originalMethod), null,
                getExceptionsFromClasses(originalMethod.getExceptionTypes())));
        this.original = originalMethod.getDeclaringClass().getName().replace(".", "/");
        alreadyRemapped.add(originalMethod);
    }

    public static boolean hasRemapped(Method method) {
        return alreadyRemapped.contains(method);
    }

    @SneakyThrows
    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        if(opcode == Opcodes.GETFIELD) {
            throw new IllegalStateException("Addon calls non-static field");
        }
        if(!writtenFields.contains(name)) {
            Field field = Class.forName(original).getField(name);
            int opcodes = Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC;
            if(Modifier.isFinal(field.getModifiers())) {
                opcodes += Opcodes.ACC_FINAL;
            }
            if(Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers())) {

                classVisitor.visitField(opcodes, name, descriptor, null, field.get(null));
                writtenFields.add(name);
            } else {
                throw new IllegalStateException("Field " + name + " isn't public and static");
            }
        }
        super.visitFieldInsn(opcode, owner, name, descriptor);
    }

    @SneakyThrows
    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        if(opcode == Opcodes.INVOKESPECIAL || opcode == Opcodes.INVOKEVIRTUAL) {
            throw new IllegalStateException("Addon calls non-static method");
        }
        if(owner.equals(Type.getType(original).getInternalName())) {
            Method found = Class.forName(owner.replace("/", ".")).getDeclaredMethod(name, Type.getClassesForDescriptor(descriptor));
            if(!hasRemapped(found)) {
                new MethodRemapper(found);
            }
            owner = "Utils";
        }
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

    private static String[] getExceptionsFromClasses(Class<?>[] classes) {
        String[] found = new String[classes.length];
        for(int i = 0; i < classes.length; i++) {
            found[i] = Type.getType(classes[i]).getInternalName();
        }
        return found;
    }
}
