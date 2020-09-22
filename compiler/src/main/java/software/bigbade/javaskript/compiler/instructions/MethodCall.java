package software.bigbade.javaskript.compiler.instructions;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import software.bigbade.javaskript.api.objects.MethodLineConverter;
import software.bigbade.javaskript.api.variables.SkriptType;
import software.bigbade.javaskript.api.variables.Type;
import software.bigbade.javaskript.compiler.utils.TypeUtils;
import software.bigbade.javaskript.compiler.variables.Loadable;

import javax.annotation.Nullable;

public class MethodCall<T> extends BasicCall<T> implements BasicInstruction {
    public MethodCall(String clazz, String method, @Nullable Type outputType, SkriptType<?>... params) {
        super(clazz, method, outputType, params);
    }

    @Override
    public void addInstructions(MethodLineConverter<?> builder, MethodVisitor code) {
        assert getClazz() != null;
        assert getMethod() != null;
        for(int i = 0; i < getParams().length; i++) {
            ((Loadable) builder.popStack()).loadVariable(builder, code);
        }

        if(getMethod().equals("<init>") || getMethod().equals("super") || getClazz().equals(builder.getClassName())) {
            code.visitMethodInsn(Opcodes.INVOKESPECIAL, getClazz().replace(".", "/"), getMethod(), TypeUtils.getMethodDescriptor(getParams(), getReturnType(), true), false);
        } else {
            code.visitMethodInsn(Opcodes.INVOKEVIRTUAL, getClazz().replace(".", "/"), getMethod(), TypeUtils.getMethodDescriptor(getParams(), getReturnType(), false), false);
        }
    }
}
