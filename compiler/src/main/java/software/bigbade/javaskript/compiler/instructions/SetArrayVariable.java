package software.bigbade.javaskript.compiler.instructions;

import lombok.RequiredArgsConstructor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import software.bigbade.javaskript.api.objects.MethodLineConverter;
import software.bigbade.javaskript.api.objects.variable.LocalVariable;
import software.bigbade.javaskript.api.variables.Type;

@RequiredArgsConstructor
public class SetArrayVariable implements BasicInstruction {
    private final LocalVariable<?> variable;
    private final int index;

    @Override
    public void addInstructions(MethodLineConverter<?> builder, MethodVisitor code) {
        Type type = builder.popStack().getType();
        builder.loadConstant(index);
        code.visitVarInsn(type.getOpcode(Opcodes.IASTORE), variable.getNumber());
    }
}
