package software.bigbade.javaskript.compiler.java;

import lombok.Getter;
import proguard.classfile.editor.CompactCodeAttributeComposer;
import software.bigbade.javaskript.compiler.instructions.BasicInstruction;
import software.bigbade.javaskript.compiler.utils.SkriptMethodBuilder;

import java.util.ArrayList;
import java.util.List;

public class BasicJavaCodeBlock implements JavaCodeBlock {
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

    public void createLabel(CompactCodeAttributeComposer code) {
        if(label == null) {
            label = code.createLabel();
            code.label(label);
        }
    }
}
