package software.bigbade.javaskript.compiler.instructions;

import lombok.RequiredArgsConstructor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import software.bigbade.javaskript.api.objects.MethodLineConverter;
import software.bigbade.javaskript.api.variables.SkriptType;
import software.bigbade.javaskript.compiler.variables.StackVariable;

import javax.annotation.Nullable;

@RequiredArgsConstructor
public class ReturnCall implements BasicInstruction {
    private final boolean returnsNothing;

    @Override
    public void addInstructions(MethodLineConverter<?> builder, MethodVisitor code) {
        if (returnsNothing) {
            code.visitInsn(Opcodes.RETURN);
            return;
        }
        StackVariable<?> value = (StackVariable<?>) builder.popStack();
        value.loadVariable(builder, code);
        code.visitInsn(value.getType().getOpcode(Opcodes.IRETURN));
    }
}
