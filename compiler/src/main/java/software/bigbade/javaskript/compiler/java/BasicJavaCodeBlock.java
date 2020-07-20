package software.bigbade.javaskript.compiler.java;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import proguard.classfile.editor.CompactCodeAttributeComposer;
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
    private CompactCodeAttributeComposer.Label label;

    private final List<BasicInstruction> instructions = new ArrayList<>();

    public void addInstruction(BasicInstruction instruction) {
        instructions.add(instruction);
    }

    @Override
    public void loadInstructions(SkriptMethodBuilder builder, CompactCodeAttributeComposer code) {
        createLabel(code);
        for(BasicInstruction instruction : instructions) {
            instruction.addInstructions(builder, code);
        }
    }

    public CompactCodeAttributeComposer.Label createLabel(CompactCodeAttributeComposer code) {
        if(label == null) {
            label = code.createLabel();
            code.label(label);
        }
        return label;
    }
}
