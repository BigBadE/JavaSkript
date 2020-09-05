package software.bigbade.javaskript.compiler.instructions;

import lombok.RequiredArgsConstructor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import software.bigbade.javaskript.api.objects.MethodLineConverter;
import software.bigbade.javaskript.api.objects.variable.LocalVariable;

@RequiredArgsConstructor
public class SetVariableCall implements BasicInstruction {
    private final LocalVariable<?> variable;

    @Override
    public void addInstructions(MethodLineConverter<?> builder, MethodVisitor code) {
        code.visitVarInsn(variable.getType().getOpcode(Opcodes.ISTORE), variable.getNumber());
    }
}
