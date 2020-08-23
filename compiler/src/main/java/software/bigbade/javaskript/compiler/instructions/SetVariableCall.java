package software.bigbade.javaskript.compiler.instructions;

import lombok.RequiredArgsConstructor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import software.bigbade.javaskript.api.objects.MethodLineConverter;
import software.bigbade.javaskript.api.objects.variable.LocalVariable;

import javax.annotation.Nullable;

@RequiredArgsConstructor
public class SetVariableCall implements BasicInstruction {
    @Nullable
    private final LocalVariable<?> variable;

    @Override
    public void addInstructions(MethodLineConverter<?> builder, MethodVisitor code) {
        if(variable == null) {
            code.visitInsn(Opcodes.RETURN);
        } else {
            code.visitVarInsn(variable.getType().getOpcode(Opcodes.ISTORE), variable.getNumber());
        }
    }
}
