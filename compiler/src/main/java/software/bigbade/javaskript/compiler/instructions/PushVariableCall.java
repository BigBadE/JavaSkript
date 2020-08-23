package software.bigbade.javaskript.compiler.instructions;

import lombok.RequiredArgsConstructor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import software.bigbade.javaskript.api.objects.MethodLineConverter;

@RequiredArgsConstructor
public class PushVariableCall implements BasicInstruction {
    private final Object value;

    @Override
    public void addInstructions(MethodLineConverter<?> builder, MethodVisitor code) {
        if (value == null) {
            code.visitInsn(Opcodes.ACONST_NULL);
        } else {
            code.visitLdcInsn(value);
        }
    }
}
