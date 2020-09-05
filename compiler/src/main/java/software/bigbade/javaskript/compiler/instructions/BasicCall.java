package software.bigbade.javaskript.compiler.instructions;

import lombok.AccessLevel;
import lombok.Getter;
import software.bigbade.javaskript.api.variables.SkriptType;
import software.bigbade.javaskript.api.variables.Type;
import software.bigbade.javaskript.compiler.variables.StackVariable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public abstract class BasicCall<T> implements BasicInstruction {
    @Getter(AccessLevel.PROTECTED)
    @Nullable
    private final Class<?> clazz;
    @Nullable
    @Getter
    private final String method;
    @Getter
    private final SkriptType[] params;
    @Nullable
    private final StackVariable<T> output;

    public BasicCall(@Nullable Class<?> clazz, @Nullable String method, @Nullable Type outputType, @Nonnull SkriptType... params) {
        this.clazz = clazz;
        this.method = method;
        this.params = params;
        if(outputType == null) {
            output = null;
        } else {
            this.output = new StackVariable<>(outputType, this::addInstructions);
        }
    }

    public Optional<StackVariable<T>> getOutput() {
        return Optional.ofNullable(output);
    }

    @Nullable
    public Type getReturnType() {
        Type outputType = null;
        if (getOutput().isPresent()) {
            outputType = getOutput().get().getType();
        }
        return outputType;
    }
}

