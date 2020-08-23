package software.bigbade.javaskript.compiler.statements;

import lombok.RequiredArgsConstructor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;


@RequiredArgsConstructor
public enum IfStatementType {
    EQUALS(Opcodes.IFEQ),
    UNEQUAL(Opcodes.IFNE),
    GREATER_THAN(Opcodes.IFGT),
    GREATER_THAN_OR_EQUAL(Opcodes.IFGE),
    LESS_THAN(Opcodes.IFLT),
    LESS_THAN_OR_EQUAL(Opcodes.IFLE),
    NULL(Opcodes.IFNULL),
    NON_NULL(Opcodes.IFNONNULL);

    private final int opcode;

    public IfStatementType inverse() {
        switch(this) {
            case EQUALS:
                return UNEQUAL;
            case UNEQUAL:
                return EQUALS;
            case GREATER_THAN:
                return LESS_THAN_OR_EQUAL;
            case GREATER_THAN_OR_EQUAL:
                return LESS_THAN;
            case LESS_THAN:
                return GREATER_THAN_OR_EQUAL;
            case LESS_THAN_OR_EQUAL:
                return GREATER_THAN;
            case NULL:
                return NON_NULL;
            case NON_NULL:
                return NULL;
        }
        throw new IllegalArgumentException("IfStatement has no inverse");
    }

    public void accept(MethodVisitor code, Label label) {
        code.visitJumpInsn(opcode, label);
    }
}
