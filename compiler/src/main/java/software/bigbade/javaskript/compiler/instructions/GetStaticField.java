package software.bigbade.javaskript.compiler.instructions;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import software.bigbade.javaskript.api.objects.MethodLineConverter;
import software.bigbade.javaskript.api.variables.Type;

public class GetStaticField<T> extends BasicCall<T> implements BasicInstruction {

    public GetStaticField(Class<?> clazz, String field, Class<T> output) {
        super(clazz, field, Type.getType(output));
    }

    @Override
    public void addInstructions(MethodLineConverter<?> builder, MethodVisitor code) {
        assert getClazz() != null;
        code.visitFieldInsn(Opcodes.GETSTATIC, getClazz().getName(), getMethod(), getOutput().orElseThrow(IllegalStateException::new).getType().getDescriptor());
    }
}
