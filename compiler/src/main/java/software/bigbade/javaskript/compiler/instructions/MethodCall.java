package software.bigbade.javaskript.compiler.instructions;

import proguard.classfile.editor.CompactCodeAttributeComposer;
import software.bigbade.javaskript.api.objects.LocalVariable;
import software.bigbade.javaskript.compiler.utils.SkriptMethodBuilder;
import software.bigbade.javaskript.compiler.utils.TypeUtils;

public class MethodCall<T> extends BasicCall<T> implements BasicInstruction {
    public MethodCall(Class<?> clazz, String method, Class<T> outputType, LocalVariable... params) {
        super(clazz, method, outputType, params);
    }

    @Override
    public void addInstructions(SkriptMethodBuilder builder, CompactCodeAttributeComposer code) {
        if(getClazz().getName().equals(builder.getParent())) {
            code.invokespecial(getClazz().getName(), getMethod(), TypeUtils.getMethodDescriptor(getParams(), getOutputType()));
        } else {
            code.invokevirtual(getClazz().getName(), getMethod(), TypeUtils.getMethodDescriptor(getParams(), getOutputType()));
        }
    }
}
