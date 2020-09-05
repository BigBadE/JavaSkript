package software.bigbade.javaskript.compiler.instructions;

import lombok.RequiredArgsConstructor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import software.bigbade.javaskript.api.objects.MethodLineConverter;
import software.bigbade.javaskript.compiler.variables.StackVariable;

@RequiredArgsConstructor
public class LoadArrayVariable implements BasicInstruction {
    private final int index;

    @Override
    public void addInstructions(MethodLineConverter<?> builder, MethodVisitor code) {
        StackVariable<?> variable = (StackVariable<?>) builder.popStack();
        code.visitVarInsn(variable.getType().getOpcode(Opcodes.IALOAD), variable.getNumber());
    }
}
