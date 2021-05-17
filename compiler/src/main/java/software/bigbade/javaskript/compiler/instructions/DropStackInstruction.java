package software.bigbade.javaskript.compiler.instructions;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import software.bigbade.javaskript.api.objects.MethodLineConverter;

public class DropStackInstruction implements BasicInstruction {
    @Override
    public void addInstructions(MethodLineConverter<?> builder, MethodVisitor code) {
        code.visitInsn(Opcodes.POP);
    }
}
