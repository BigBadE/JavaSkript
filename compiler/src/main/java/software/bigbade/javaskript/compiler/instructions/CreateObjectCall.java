package software.bigbade.javaskript.compiler.instructions;

import proguard.classfile.editor.CompactCodeAttributeComposer;
import software.bigbade.javaskript.api.objects.LocalVariable;
import software.bigbade.javaskript.compiler.utils.SkriptMethodBuilder;
import software.bigbade.javaskript.compiler.utils.TypeUtils;

import javax.annotation.Nullable;

public class CreateObjectCall<T> extends BasicCall<T> implements BasicInstruction {
    @Nullable
    private final String name;

    public CreateObjectCall(Class<T> clazz, @Nullable String name, LocalVariable... params) {
        super(clazz, null, clazz, params);
        this.name = name;
    }

    @Override
    public void addInstructions(SkriptMethodBuilder builder, CompactCodeAttributeComposer code) {
        code.new_(getClazz().getName());
        code.dup();
        for(LocalVariable variable : getParams()) {
            new LoadVariableCall(variable).addInstructions(builder, code);
        }
        code.invokespecial(getClazz().getName(), "<init>", TypeUtils.getMethodDescriptor(getParams(), null));
        setOutput(builder, name);
    }
}
