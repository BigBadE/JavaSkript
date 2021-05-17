package software.bigbade.javaskript.compiler.java;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import software.bigbade.javaskript.compiler.instructions.BasicInstruction;
import software.bigbade.javaskript.compiler.utils.SkriptMethodBuilder;

import javax.annotation.Nullable;

public interface JavaCodeBlock {
    @Nullable
    JavaCodeBlock getParent();

    void setParent(@Nullable JavaCodeBlock block);

    Label getLabel();

    void addInstruction(BasicInstruction instruction);

    void loadInstructions(SkriptMethodBuilder<?> builder, MethodVisitor visitor);

    Label createLabel(MethodVisitor visitor);
}
