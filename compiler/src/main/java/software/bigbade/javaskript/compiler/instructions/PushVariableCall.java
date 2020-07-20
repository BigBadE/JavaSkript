package software.bigbade.javaskript.compiler.instructions;

import lombok.RequiredArgsConstructor;
import proguard.classfile.editor.CompactCodeAttributeComposer;
import software.bigbade.javaskript.compiler.utils.SkriptMethodBuilder;

@RequiredArgsConstructor
public class PushVariableCall<T> implements BasicInstruction {
    private final T value;

    @Override
    public void addInstructions(SkriptMethodBuilder builder, CompactCodeAttributeComposer code) {
        if (value == null) {
            code.aconst_null();
        } else {
            code.ldc(value);
        }
    }
}
