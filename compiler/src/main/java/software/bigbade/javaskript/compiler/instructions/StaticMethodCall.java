package software.bigbade.javaskript.compiler.instructions;

import proguard.classfile.editor.CompactCodeAttributeComposer;
import software.bigbade.javaskript.api.objects.LocalVariable;
import software.bigbade.javaskript.compiler.utils.SkriptMethodBuilder;
import software.bigbade.javaskript.compiler.utils.TypeUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class StaticMethodCall<T> extends BasicCall<T> implements BasicInstruction {
    @Nullable
    private final String name;

    public StaticMethodCall(@Nonnull Class<?> clazz, @Nonnull String method, @Nullable Class<T> outputType, @Nullable String name, @Nonnull LocalVariable... params) {
        super(clazz, method, outputType, params);
        this.name = name;
    }

    @Override
    public void addInstructions(SkriptMethodBuilder builder, CompactCodeAttributeComposer code) {
        code.invokestatic(getClazz().getName(), getMethod(), TypeUtils.getMethodDescriptor(getParams(), getOutputType()));
        setOutput(builder, name);
    }
}
