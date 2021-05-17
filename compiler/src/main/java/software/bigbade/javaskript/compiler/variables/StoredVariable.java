package software.bigbade.javaskript.compiler.variables;

import lombok.Getter;
import lombok.Setter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import software.bigbade.javaskript.api.objects.MethodLineConverter;
import software.bigbade.javaskript.api.objects.variable.LocalVariable;
import software.bigbade.javaskript.api.variables.Type;

@Getter
public class StoredVariable<T> implements LocalVariable<T>, Loadable {
    @Setter
    private int number;
    @Setter
    private Type type;

    public StoredVariable(Class<T> clazz) {
        this.type = Type.getType(clazz);
    }

    public StoredVariable(Type type) {
        this.type = type;
    }

    public void loadVariable(MethodLineConverter<?> converter, MethodVisitor code) {
        code.visitVarInsn(type.getOpcode(Opcodes.ILOAD), number);
    }
}
