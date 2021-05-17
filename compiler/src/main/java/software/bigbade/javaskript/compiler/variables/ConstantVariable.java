package software.bigbade.javaskript.compiler.variables;

import lombok.Getter;
import lombok.Setter;
import org.objectweb.asm.MethodVisitor;
import software.bigbade.javaskript.api.objects.MethodLineConverter;
import software.bigbade.javaskript.api.objects.variable.LocalVariable;
import software.bigbade.javaskript.api.variables.Type;

@Getter
public class ConstantVariable<T> implements LocalVariable<T>, Loadable {
    private final T constant;
    @Setter
    private Type type;

    @Setter
    private int number;

    public ConstantVariable(T constant) {
        this.constant = constant;
        this.type = Type.getType(constant.getClass());
    }

    @Override
    public void loadVariable(MethodLineConverter<?> converter, MethodVisitor code) {
        code.visitLdcInsn(constant);
    }
}
