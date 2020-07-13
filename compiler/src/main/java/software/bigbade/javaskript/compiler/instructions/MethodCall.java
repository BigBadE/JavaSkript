package software.bigbade.javaskript.compiler.instructions;

import proguard.classfile.editor.CompactCodeAttributeComposer;
import software.bigbade.javaskript.compiler.utils.SkriptMethodBuilder;
import software.bigbade.javaskript.compiler.utils.Type;

public class MethodCall<T> extends BasicCall<T> implements BasicInstruction {
    public MethodCall(Class<?> clazz, String method, Class<T> outputType, LocalVariable... params) {
        super(clazz, method, outputType, params);
    }

    @Override
    public void addInstructions(SkriptMethodBuilder builder, CompactCodeAttributeComposer code) {
        StringBuilder descriptionBuilder = new StringBuilder();
        descriptionBuilder.append("(");
        for (LocalVariable variable : getParams()) {
            variable.getType().getDescriptor(descriptionBuilder);
        }
        descriptionBuilder.append(")");
        if(getOutputType() == null) {
            descriptionBuilder.append("V");
        } else {
            Type.getType(getOutputType()).getDescriptor(descriptionBuilder);
        }
        if(getClazz().getName().equals(builder.getParent())) {
            code.invokespecial(getClazz().getName(), getMethod(), descriptionBuilder.toString());
        } else {
            code.invokevirtual(getClazz().getName(), getMethod(), descriptionBuilder.toString());
        }
    }
}
