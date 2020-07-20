package software.bigbade.javaskript.compiler.instructions;

import lombok.RequiredArgsConstructor;
import proguard.classfile.editor.CompactCodeAttributeComposer;
import software.bigbade.javaskript.api.instructions.VariableChanges;
import software.bigbade.javaskript.api.objects.LocalVariable;
import software.bigbade.javaskript.compiler.utils.SkriptMethodBuilder;

@RequiredArgsConstructor
public class MathCall implements BasicInstruction {
    private final VariableChanges operation;
    private final LocalVariable first;
    private final LocalVariable second;

    @Override
    public void addInstructions(SkriptMethodBuilder builder, CompactCodeAttributeComposer code) {
        new LoadVariableCall(first).addInstructions(builder, code);
        new LoadVariableCall(second).addInstructions(builder, code);
        switch (operation) {
            case MULTIPLY:
                code.imul();
        }
    }
}
