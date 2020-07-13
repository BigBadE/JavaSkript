package software.bigbade.javaskript.compiler.instructions;

import lombok.RequiredArgsConstructor;
import proguard.classfile.editor.CompactCodeAttributeComposer;
import software.bigbade.javaskript.compiler.utils.SkriptMethodBuilder;
import software.bigbade.javaskript.compiler.utils.Type;

@RequiredArgsConstructor
public class LoadVariableCall implements BasicInstruction {
    private final LocalVariable variable;

    @Override
    public void addInstructions(SkriptMethodBuilder builder, CompactCodeAttributeComposer code) {
        if(variable.getType().equals(Type.LONG_TYPE)) {
            code.lload(variable.getNumber());
        } else if(variable.getType().equals(Type.FLOAT_TYPE)) {
            code.fload(variable.getNumber());
        } else if(variable.getType().equals(Type.INT_TYPE)) {
            code.iload(variable.getNumber());
        } else if(variable.getType().equals(Type.DOUBLE_TYPE)) {
            code.dload(variable.getNumber());
        } else {
            code.aload(variable.getNumber());
        }
    }
}
