package software.bigbade.javaskript.compiler.instructions;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import software.bigbade.javaskript.api.objects.MethodLineConverter;
import software.bigbade.javaskript.api.variables.SkriptType;
import software.bigbade.javaskript.api.variables.Type;
import software.bigbade.javaskript.compiler.utils.TypeUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class StaticMethodCall<T> extends BasicCall<T> implements BasicInstruction {

    public StaticMethodCall(@Nonnull String clazz, @Nonnull String method, @Nullable Type outputType, @Nonnull SkriptType<?>... params) {
        super(clazz, method, outputType, params);
    }

    @Override
    public void addInstructions(MethodLineConverter<?> builder, MethodVisitor code) {
        assert getClazz() != null;
        code.visitMethodInsn(Opcodes.INVOKESTATIC, getClazz(), getMethod(), TypeUtils.getMethodDescriptor(getParams(), getReturnType(), false), false);
    }
}