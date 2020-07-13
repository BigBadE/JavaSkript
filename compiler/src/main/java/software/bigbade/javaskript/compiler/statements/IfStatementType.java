package software.bigbade.javaskript.compiler.statements;

import lombok.RequiredArgsConstructor;
import proguard.classfile.editor.CompactCodeAttributeComposer;

import java.util.function.BiConsumer;

@RequiredArgsConstructor
public enum IfStatementType {
    EQUALS(CompactCodeAttributeComposer::ifeq),
    UNEQUAL(CompactCodeAttributeComposer::ifne),
    GREATER_THAN(CompactCodeAttributeComposer::ifgt),
    GREATER_THAN_OR_EQUAL(CompactCodeAttributeComposer::ifge),
    LESS_THAN(CompactCodeAttributeComposer::iflt),
    LESS_THAN_OR_EQUAL(CompactCodeAttributeComposer::ifle),
    NULL(CompactCodeAttributeComposer::ifnull),
    NON_NULL(CompactCodeAttributeComposer::ifnonnull);

    private final BiConsumer<CompactCodeAttributeComposer, CompactCodeAttributeComposer.Label> function;

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

    public void accept(CompactCodeAttributeComposer code, CompactCodeAttributeComposer.Label label) {
        function.accept(code, label);
    }
}
