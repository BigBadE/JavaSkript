package software.bigbade.javaskript.compiler.instructions;

import lombok.RequiredArgsConstructor;
import proguard.classfile.editor.CompactCodeAttributeComposer;
import software.bigbade.javaskript.api.objects.LocalVariable;
import software.bigbade.javaskript.api.variables.Type;
import software.bigbade.javaskript.compiler.utils.SkriptMethodBuilder;

@RequiredArgsConstructor
public class SetVariableCall implements BasicInstruction {
    private final LocalVariable variable;

    @Override
    public void addInstructions(SkriptMethodBuilder builder, CompactCodeAttributeComposer code) {
        if(variable.getType().equals(Type.LONG_TYPE)) {
            code.lstore(variable.getNumber());
        } else if(variable.getType().equals(Type.FLOAT_TYPE)) {
            code.fstore(variable.getNumber());
        } else if(variable.getType().equals(Type.INT_TYPE)) {
            code.istore(variable.getNumber());
        } else if(variable.getType().equals(Type.DOUBLE_TYPE)) {
            code.dstore(variable.getNumber());
        } else {
            code.astore(variable.getNumber());
        }
    }
}
