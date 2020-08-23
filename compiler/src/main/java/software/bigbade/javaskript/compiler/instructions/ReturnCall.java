package software.bigbade.javaskript.compiler.instructions;

import lombok.RequiredArgsConstructor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import software.bigbade.javaskript.api.objects.MethodLineConverter;
import software.bigbade.javaskript.api.objects.variable.LocalVariable;
import software.bigbade.javaskript.compiler.variables.StackVariable;

import javax.annotation.Nullable;

@RequiredArgsConstructor
public class ReturnCall implements BasicInstruction {
    @Nullable
    private final LocalVariable<?> value;

    @Override
    public void addInstructions(MethodLineConverter<?> builder, MethodVisitor code) {
        ((StackVariable<?>) builder.getStack()).loadVariable(builder, code);
        if(value == null || value.getType() == null) {
            code.visitInsn(Opcodes.RETURN);
        } else {
            code.visitInsn(value.getType().getOpcode(Opcodes.IRETURN));
        }
    }
}
