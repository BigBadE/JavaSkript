package software.bigbade.javaskript.compiler.instructions;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import software.bigbade.javaskript.api.objects.MethodLineConverter;
import software.bigbade.javaskript.api.objects.variable.LocalVariable;
import software.bigbade.javaskript.api.variables.Type;
import software.bigbade.javaskript.compiler.variables.Loadable;

public class CreateObjectCall<T> extends BasicCall<T> {
    public CreateObjectCall(Class<T> clazz, LocalVariable<?>... params) {
        super(clazz, "<init>", Type.getType(clazz), params);
    }

    @Override
    public void addInstructions(MethodLineConverter<?> builder, MethodVisitor code) {
        assert getClazz() != null;
        code.visitTypeInsn(Opcodes.NEW, getClazz().getName());
        code.visitInsn(Opcodes.DUP);
        for(LocalVariable<?> variable : getParams()) {
            ((Loadable) variable).loadVariable(builder, code);
        }
        new MethodCall<>(getClazz(), "<init>", getReturnType(), getParams()).addInstructions(builder, code);
    }
}
