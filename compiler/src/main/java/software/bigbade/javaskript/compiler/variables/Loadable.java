package software.bigbade.javaskript.compiler.variables;

import org.objectweb.asm.MethodVisitor;
import software.bigbade.javaskript.api.objects.MethodLineConverter;

public interface Loadable {
    void loadVariable(MethodLineConverter<?> converter, MethodVisitor code);
}
