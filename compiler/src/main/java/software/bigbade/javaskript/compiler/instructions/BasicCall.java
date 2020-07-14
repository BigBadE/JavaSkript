package software.bigbade.javaskript.compiler.instructions;

import lombok.Getter;
import software.bigbade.javaskript.compiler.utils.SkriptMethodBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BasicCall<T> {
    @Getter
    @Nonnull
    private final Class<?> clazz;
    @Nullable
    @Getter
    private final String method;
    @Nullable
    @Getter
    private final Class<T> outputType;
    @Getter
    private final LocalVariable[] params;

    public BasicCall(@Nonnull Class<?> clazz, @Nullable String method, @Nullable Class<T> outputType, @Nonnull LocalVariable... params) {
        this.clazz = clazz;
        this.method = method;
        this.outputType = outputType;
        this.params = params;
    }

    @Getter
    @Nullable
    protected LocalVariable output;

    protected void setOutput(SkriptMethodBuilder builder) {
        output = new LocalVariable(builder.getLocalVariables(), outputType);
        builder.setLocalVariables(builder.getLocalVariables()+1);
    }
}

