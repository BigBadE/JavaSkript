package software.bigbade.javaskript.compiler.utils;

import lombok.SneakyThrows;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import software.bigbade.javaskript.api.objects.MethodLineConverter;
import software.bigbade.javaskript.api.variables.Type;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

public class MethodRemapper extends MethodVisitor {
    private final MethodLineConverter<?> builder;
    private final Set<String> writtenFields = new HashSet<>();
    private final ClassVisitor classVisitor;
    private final String newClass;
    private final String original;

    public MethodRemapper(MethodLineConverter<?> builder, ClassVisitor classVisitor, MethodVisitor code, String original) {
        super(Opcodes.ASM9, code);
        this.newClass = "LUtils";
        this.classVisitor = classVisitor;
        this.original = original;
        this.builder = builder;
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

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        if(opcode == Opcodes.INVOKESPECIAL || opcode == Opcodes.INVOKEVIRTUAL) {
            throw new IllegalStateException("Addon calls non-static method");
        }
        if(owner.equals(Type.getType(original).getInternalName())) {
            new MethodRemapper(builder, classVisitor, super.mv, original);
            owner = newClass;
        }
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }
}
