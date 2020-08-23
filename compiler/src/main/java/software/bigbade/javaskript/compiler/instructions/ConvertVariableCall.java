package software.bigbade.javaskript.compiler.instructions;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import software.bigbade.javaskript.api.objects.MethodLineConverter;
import software.bigbade.javaskript.api.objects.variable.LocalVariable;
import software.bigbade.javaskript.api.variables.Type;
import software.bigbade.javaskript.compiler.utils.UnboxUtils;
import software.bigbade.javaskript.compiler.variables.Loadable;

import java.util.Arrays;
import java.util.List;

public class ConvertVariableCall<T> extends BasicCall<T> {
    private final LocalVariable<?> variable;

    private final List<Type> types = Arrays.asList(Type.INT_TYPE, Type.LONG_TYPE, Type.FLOAT_TYPE, Type.DOUBLE_TYPE);

    public ConvertVariableCall(LocalVariable<?> variable, Type convertTo) {
        super(null, null, convertTo, variable);
        this.variable = variable;
    }

    @Override
    public void addInstructions(MethodLineConverter<?> builder, MethodVisitor code) {
        ((Loadable) variable).loadVariable(builder, code);
        int opcode;
        //Bytes, chars and shorts can only come from ints, convert other types to ints first.
        assert getReturnType() != null;
        if(getReturnType().equals(Type.BYTE_TYPE)) {
            confirmInt(builder, code);
            opcode = Opcodes.I2B;
        } else if(getReturnType().equals(Type.CHAR_TYPE)) {
            confirmInt(builder, code);
            opcode = Opcodes.I2C;
        } else if(getReturnType().equals(Type.SHORT_TYPE)) {
            confirmInt(builder, code);
            opcode = Opcodes.I2S;
            //Boxed types, such as java.lang.Integer, should be unboxed instead of checkcast'd.
        } else if(UnboxUtils.isBoxed(variable.getType())) {
            UnboxUtils.unbox(variable, code);
            return;
            //Unboxed types, being boxed, should use the box method instead of checkcast.
        } else if(UnboxUtils.isBoxed(getReturnType())) {
            UnboxUtils.box(variable, code);
            return;
        } else {
            int firstIndex = types.indexOf(variable.getType());
            int secondIndex = types.indexOf(getReturnType());
            if (firstIndex >= 0 && secondIndex >= 0) {
                opcode = 133 + (firstIndex * 3 + ((firstIndex < secondIndex) ? secondIndex - 1 : secondIndex));
            } else {
                code.visitLdcInsn(getReturnType().getInternalName());
                code.visitVarInsn(Opcodes.CHECKCAST, variable.getNumber());
                return;
            }
        }
        code.visitInsn(opcode);
    }

    private void confirmInt(MethodLineConverter<?> builder, MethodVisitor code) {
        if(!variable.getType().equals(Type.INT_TYPE)) {
            new ConvertVariableCall<>(variable, Type.INT_TYPE).addInstructions(builder, code);
        }
    }
}
