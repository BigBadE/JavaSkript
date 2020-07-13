package software.bigbade.javaskript.compiler.instructions;

import proguard.classfile.editor.CompactCodeAttributeComposer;
import software.bigbade.javaskript.compiler.utils.SkriptMethodBuilder;
import software.bigbade.javaskript.compiler.utils.Type;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class StaticMethodCall<T> extends BasicCall<T> implements BasicInstruction {
    public StaticMethodCall(@Nonnull Class<?> clazz, @Nonnull String method, @Nullable Class<T> outputType, @Nonnull LocalVariable... params) {
        super(clazz, method, outputType, params);
    }

    @Override
    public void addInstructions(SkriptMethodBuilder builder, CompactCodeAttributeComposer code) {
        StringBuilder descriptorBuilder = new StringBuilder();
        descriptorBuilder.append("(");
        for (LocalVariable param : getParams()) {
            param.getType().getDescriptor(descriptorBuilder);
        }
        descriptorBuilder.append(")");
        if(getOutputType() == null) {
            descriptorBuilder.append("V");
        } else {
            Type.getType(getOutputType()).getDescriptor(descriptorBuilder);
        }
        code.invokestatic(getClazz().getName(), getMethod(), descriptorBuilder.toString());
        setOutput(builder);
    }
}
