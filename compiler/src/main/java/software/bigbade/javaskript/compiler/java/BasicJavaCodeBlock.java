package software.bigbade.javaskript.compiler.java;

import lombok.Getter;
import lombok.Setter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import software.bigbade.javaskript.compiler.instructions.BasicInstruction;
import software.bigbade.javaskript.compiler.utils.SkriptMethodBuilder;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BasicJavaCodeBlock implements JavaCodeBlock {
    @Getter
    @Setter
    @Nullable
    private JavaCodeBlock parent;

    @Getter
    private Label label;

    private final List<BasicInstruction> instructions = new ArrayList<>();

    public void addInstruction(BasicInstruction instruction) {
        instructions.add(instruction);
    }

    @Override
    public void loadInstructions(SkriptMethodBuilder<?> builder, MethodVisitor code) {
        createLabel(code);
        for(BasicInstruction instruction : instructions) {
            instruction.addInstructions(builder, code);
        }
    }

    public Label createLabel(MethodVisitor code) {
        if(label == null) {
            label = new Label();
            code.visitLabel(label);
        }
        return label;
    }
}
