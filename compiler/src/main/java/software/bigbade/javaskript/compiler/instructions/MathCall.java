package software.bigbade.javaskript.compiler.instructions;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import software.bigbade.javaskript.api.instructions.VariableChanges;
import software.bigbade.javaskript.api.objects.MethodLineConverter;
import software.bigbade.javaskript.api.objects.variable.LocalVariable;
import software.bigbade.javaskript.compiler.variables.Loadable;

public class MathCall<T> extends BasicCall<T> {
    private final VariableChanges operation;
    private final LocalVariable<T> first;
    private final LocalVariable<?> second;

    public MathCall(VariableChanges operation, LocalVariable<T> first, LocalVariable<?> second) {
        super(null, null, first.getType(), second);
        this.operation = operation;
        this.first = first;
        this.second = second;
    }

    @Override
    public void addInstructions(MethodLineConverter<?> builder, MethodVisitor code) {
        if(first.getType().equals(second.getType())) {
            ((Loadable) second).loadVariable(builder, code);
        } else {
            ConvertVariableCall<T> conversion = new ConvertVariableCall<>(second, first.getType());
            conversion.getOutput().orElseThrow(IllegalStateException::new).loadVariable(builder, code);
            second.setType(first.getType());
        }
        //Due to the stack being FILO (First in last out), first needs to be loaded second.
        ((Loadable) first).loadVariable(builder, code);
        switch (operation) {
            case ADD:
                code.visitInsn(first.getType().getOpcode(Opcodes.IADD));
                break;
            case SUBTRACT:
                code.visitInsn(first.getType().getOpcode(Opcodes.ISUB));
                break;
            case MULTIPLY:
                code.visitInsn(first.getType().getOpcode(Opcodes.IMUL));
                break;
            case DIVIDE:
                code.visitInsn(first.getType().getOpcode(Opcodes.IDIV));
                break;
            case NEGATE:
                code.visitInsn(first.getType().getOpcode(Opcodes.INEG));
        }
    }
}
