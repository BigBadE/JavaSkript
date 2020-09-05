package software.bigbade.javaskript.compiler.statements;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;


@RequiredArgsConstructor
public enum IfStatementType {
    EQUALS(Opcodes.IFEQ, 1),
    UNEQUAL(Opcodes.IFNE, 1),
    GREATER_THAN(Opcodes.IFGT, 2),
    GREATER_THAN_OR_EQUAL(Opcodes.IFGE, 2),
    LESS_THAN(Opcodes.IFLT, 2),
    LESS_THAN_OR_EQUAL(Opcodes.IFLE, 2),
    NULL(Opcodes.IFNULL, 1),
    NON_NULL(Opcodes.IFNONNULL, 1);

    private final int opcode;
    @Getter
    private final int args;

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
