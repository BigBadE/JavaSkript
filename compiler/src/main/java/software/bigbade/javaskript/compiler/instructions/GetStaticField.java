package software.bigbade.javaskript.compiler.instructions;

import proguard.classfile.editor.CompactCodeAttributeComposer;
import software.bigbade.javaskript.compiler.utils.SkriptMethodBuilder;
import software.bigbade.javaskript.compiler.utils.Type;

public class GetStaticField<T> extends BasicCall<T> implements BasicInstruction {
    public GetStaticField(Class<?> clazz, String field, Class<T> output) {
        super(clazz, field, output);
    }

    @Override
    public void addInstructions(SkriptMethodBuilder builder, CompactCodeAttributeComposer code) {
        code.getstatic(getClazz().getName(), getMethod(), Type.getDescriptor(getOutputType()));
        setOutput(builder);
    }
}
