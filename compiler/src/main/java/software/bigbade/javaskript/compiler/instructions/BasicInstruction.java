package software.bigbade.javaskript.compiler.instructions;

import org.objectweb.asm.MethodVisitor;
import software.bigbade.javaskript.api.objects.MethodLineConverter;
import software.bigbade.javaskript.compiler.utils.SkriptMethodBuilder;

public interface BasicInstruction {
    void addInstructions(MethodLineConverter<?> builder, MethodVisitor code);
}
