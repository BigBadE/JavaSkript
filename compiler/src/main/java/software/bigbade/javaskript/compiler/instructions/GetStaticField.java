package software.bigbade.javaskript.compiler.instructions;

import proguard.classfile.editor.CompactCodeAttributeComposer;
import software.bigbade.javaskript.compiler.utils.SkriptMethodBuilder;
import software.bigbade.javaskript.api.variables.Type;

import javax.annotation.Nullable;

public class GetStaticField<T> extends BasicCall<T> implements BasicInstruction {
    @Nullable
    private final String name;

    public GetStaticField(Class<?> clazz, String field, Class<T> output, String name) {
        super(clazz, field, output);
        this.name = name;
    }

    @Override
    public void addInstructions(SkriptMethodBuilder builder, CompactCodeAttributeComposer code) {
        code.getstatic(getClazz().getName(), getMethod(), Type.getDescriptor(getOutputType()));
        setOutput(builder, name);
    }
}
