package software.bigbade.javaskript.compiler.instructions;

import org.objectweb.asm.MethodVisitor;
import software.bigbade.javaskript.api.objects.MethodLineConverter;
import software.bigbade.javaskript.api.variables.Type;
import software.bigbade.javaskript.compiler.variables.Loadable;

import javax.annotation.Nonnull;

public class InstanceofCall extends BasicCall<Boolean>{
    private final Type output;
    public InstanceofCall(@Nonnull Type output) {
        super(null, null, Type.BOOLEAN_TYPE);
        this.output = output;
    }

    @Override
    public void addInstructions(MethodLineConverter<?> builder, MethodVisitor code) {
        ((Loadable) builder.popStack()).loadVariable(builder, code);
        builder.loadConstant(output.getDescriptor());

    }
}
