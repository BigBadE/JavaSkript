package software.bigbade.javaskript.compiler.instructions;

import lombok.RequiredArgsConstructor;
import proguard.classfile.editor.CompactCodeAttributeComposer;
import software.bigbade.javaskript.api.objects.LocalVariable;
import software.bigbade.javaskript.api.variables.Type;
import software.bigbade.javaskript.compiler.utils.SkriptMethodBuilder;

@RequiredArgsConstructor
public class ConvertVariableCall<T> implements BasicInstruction {
    private final LocalVariable variable;
    private final Class<T> convertTo;

    @Override
    public void addInstructions(SkriptMethodBuilder builder, CompactCodeAttributeComposer code) {
        new LoadVariableCall(variable).addInstructions(builder, code);
        if(variable.getType().equals(Type.LONG_TYPE)) {
            if(convertTo.equals(Float.TYPE)) {
                code.l2f();
            } else if(convertTo.equals(Integer.TYPE)) {
                code.l2i();
            } else if(convertTo.equals(Double.TYPE)) {
                code.l2d();
            } else {
                code.checkcast(variable.getNumber());
            }
        } else if(variable.getType().equals(Type.FLOAT_TYPE)) {
            if(convertTo.equals(Long.TYPE)) {
                code.f2l();
            } else if(convertTo.equals(Integer.TYPE)) {
                code.f2i();
            } else if(convertTo.equals(Double.TYPE)) {
                code.f2d();
            } else {
                code.checkcast(variable.getNumber());
            }
        } else if(variable.getType().equals(Type.INT_TYPE)) {
            if(convertTo.equals(Long.TYPE)) {
                code.i2l();
            } else if(convertTo.equals(Float.TYPE)) {
                code.i2f();
            } else if(convertTo.equals(Double.TYPE)) {
                code.i2d();
            } else if(convertTo.equals(Byte.TYPE)) {
                code.i2b();
            } else if(convertTo.equals(Character.TYPE)) {
                code.i2c();
            } else if(convertTo.equals(Short.TYPE)) {
                code.i2s();
            } else {
                code.checkcast(variable.getNumber());
            }
        } else if(variable.getType().equals(Type.DOUBLE_TYPE)) {
            if(convertTo.equals(Long.TYPE)) {
                code.d2l();
            } else if(convertTo.equals(Integer.TYPE)) {
                code.d2i();
            } else if(convertTo.equals(Float.TYPE)) {
                code.d2f();
            } else {
                code.checkcast(variable.getNumber());
            }
        } else {
            code.checkcast(variable.getNumber());
        }
        new SetVariableCall(variable).addInstructions(builder, code);
    }
}
