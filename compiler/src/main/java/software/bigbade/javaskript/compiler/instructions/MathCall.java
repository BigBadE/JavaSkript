package software.bigbade.javaskript.compiler.instructions;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import software.bigbade.javaskript.api.instructions.VariableChanges;
import software.bigbade.javaskript.api.objects.MethodLineConverter;
import software.bigbade.javaskript.api.variables.SkriptType;
import software.bigbade.javaskript.compiler.variables.Loadable;
import software.bigbade.javaskript.compiler.variables.StackVariable;

public class MathCall<T> extends BasicCall<T> {
    private final VariableChanges operation;

    public MathCall(VariableChanges operation, SkriptType first, SkriptType second) {
        super(null, null, first.getType(), first, second);
        this.operation = operation;
    }

    @Override
    public void addInstructions(MethodLineConverter<?> builder, MethodVisitor code) {
        if(getParams().length == 2) {
            if (getParams()[0].getType().equals(getParams()[1].getType())) {
                StackVariable<?> second = (StackVariable<?>) builder.popStack();
                ((Loadable) second).loadVariable(builder, code);
            } else {
                ConvertVariableCall<T> conversion = new ConvertVariableCall<>(getParams()[1], getParams()[0].getType());
                conversion.getOutput().orElseThrow(IllegalStateException::new).loadVariable(builder, code);
            }
        }
        //Due to the stack being FILO (First in last out), first needs to be loaded second.
        ((Loadable) builder.popStack()).loadVariable(builder, code);
        switch (operation) {
            case ADD:
                code.visitInsn(getParams()[0].getType().getOpcode(Opcodes.IADD));
                break;
            case SUBTRACT:
                code.visitInsn(getParams()[0].getType().getOpcode(Opcodes.ISUB));
                break;
            case MULTIPLY:
                code.visitInsn(getParams()[0].getType().getOpcode(Opcodes.IMUL));
                break;
            case DIVIDE:
                code.visitInsn(getParams()[0].getType().getOpcode(Opcodes.IDIV));
                break;
            case MODULO:
                code.visitInsn(getParams()[0].getType().getOpcode(Opcodes.IDIV));
                code.visitInsn(getParams()[0].getType().getOpcode(Opcodes.IREM));
                break;
            case NEGATE:
                code.visitInsn(getParams()[0].getType().getOpcode(Opcodes.INEG));
                break;
            case OR:
                code.visitInsn(getParams()[0].getType().getOpcode(Opcodes.IOR));
                break;
            case AND:
                code.visitInsn(getParams()[0].getType().getOpcode(Opcodes.IAND));
                break;
            case XOR:
                code.visitInsn(getParams()[0].getType().getOpcode(Opcodes.IXOR));
                break;
            case LEFTSHIFT:
                code.visitInsn(getParams()[0].getType().getOpcode(Opcodes.ISHR));
                break;
            case RIGHTSHIFT:
                code.visitInsn(getParams()[0].getType().getOpcode(Opcodes.ISHL));
        }
    }
}
