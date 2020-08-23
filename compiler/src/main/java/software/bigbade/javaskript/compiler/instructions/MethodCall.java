package software.bigbade.javaskript.compiler.instructions;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import software.bigbade.javaskript.api.objects.MethodLineConverter;
import software.bigbade.javaskript.api.objects.variable.LocalVariable;
import software.bigbade.javaskript.api.variables.Type;
import software.bigbade.javaskript.compiler.utils.TypeUtils;
import software.bigbade.javaskript.compiler.variables.Loadable;

import javax.annotation.Nullable;

public class MethodCall<T> extends BasicCall<T> implements BasicInstruction {
    public MethodCall(Class<?> clazz, String method, @Nullable Type outputType, LocalVariable<?>... params) {
        super(clazz, method, outputType, params);
    }

    @Override
    public void addInstructions(MethodLineConverter<?> builder, MethodVisitor code) {
        assert getClazz() != null;
        assert getMethod() != null;
        for(int i = getParams().length; i > 0; i--) {
            ((Loadable) getParams()[i-1]).loadVariable(builder, code);
        }

        if(getMethod().equals("<init>") || getMethod().equals("super") || getClazz().getName().equals(builder.getClassName())) {
            code.visitMethodInsn(Opcodes.INVOKESPECIAL, getClazz().getName().replace(".", "/"), getMethod(), TypeUtils.getMethodDescriptor(getParams(), getReturnType()), false);
        } else {
            code.visitMethodInsn(Opcodes.INVOKEVIRTUAL, getClazz().getName().replace(".", "/"), getMethod(), TypeUtils.getMethodDescriptor(getParams(), getReturnType()), false);
        }
    }
}
