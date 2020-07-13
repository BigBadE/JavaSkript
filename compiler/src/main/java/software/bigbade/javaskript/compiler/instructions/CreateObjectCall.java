package software.bigbade.javaskript.compiler.instructions;

import proguard.classfile.editor.CompactCodeAttributeComposer;
import software.bigbade.javaskript.compiler.utils.SkriptMethodBuilder;

public class CreateObjectCall<T> extends BasicCall<T> implements BasicInstruction {
    public CreateObjectCall(Class<T> clazz, LocalVariable... params) {
        super(clazz, null, clazz, params);
    }

    @Override
    public void addInstructions(SkriptMethodBuilder builder, CompactCodeAttributeComposer code) {
        code.new_(getClazz().getName());
        code.dup();
        StringBuilder descriptorBuilder = new StringBuilder();
        descriptorBuilder.append("(");
        for (LocalVariable param : getParams()) {
            param.getType().getDescriptor(descriptorBuilder);
        }
        descriptorBuilder.append(")V");
        code.invokespecial(getClazz().getName(), "<init>", descriptorBuilder.toString());
        setOutput(builder);
    }
}
