package software.bigbade.javaskript.compiler.variables;

import lombok.Getter;
import lombok.Setter;
import org.objectweb.asm.MethodVisitor;
import software.bigbade.javaskript.api.objects.MethodLineConverter;
import software.bigbade.javaskript.api.objects.variable.LocalVariable;
import software.bigbade.javaskript.api.variables.Type;

import java.util.function.BiConsumer;

@Getter
@Setter
public class StackVariable<T> implements LocalVariable<T>, Loadable {
    private final BiConsumer<MethodLineConverter<?>, MethodVisitor> consumer;
    private Type type;
    private int number;

    public StackVariable(Type type, BiConsumer<MethodLineConverter<?>, MethodVisitor> consumer) {
        this.type = type;
        this.consumer = consumer;
    }

    //Any methods must be delayed until needed, to get the correct stack order.
    @Override
    public void loadVariable(MethodLineConverter<?> converter, MethodVisitor visitor) {
        consumer.accept(converter, visitor);
    }
}
