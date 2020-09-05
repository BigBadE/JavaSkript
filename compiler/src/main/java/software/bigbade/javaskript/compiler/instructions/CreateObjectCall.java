package software.bigbade.javaskript.compiler.instructions;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import software.bigbade.javaskript.api.objects.MethodLineConverter;
import software.bigbade.javaskript.api.variables.SkriptType;
import software.bigbade.javaskript.api.variables.Type;

public class CreateObjectCall<T> extends BasicCall<T> {
    public CreateObjectCall(Class<T> clazz, SkriptType<?>... params) {
        super(clazz, "<init>", Type.getType(clazz), params);
    }

    @Override
    public void addInstructions(MethodLineConverter<?> builder, MethodVisitor code) {
        assert getClazz() != null;
        code.visitTypeInsn(Opcodes.NEW, getClazz().getName());
        code.visitInsn(Opcodes.DUP);
        new MethodCall<>(getClazz(), "<init>", getReturnType(), getParams()).addInstructions(builder, code);
    }
}
