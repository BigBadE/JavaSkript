package software.bigbade.javaskript.compiler.java;

import proguard.classfile.editor.CompactCodeAttributeComposer;
import software.bigbade.javaskript.compiler.instructions.BasicInstruction;
import software.bigbade.javaskript.compiler.utils.SkriptMethodBuilder;

public interface JavaCodeBlock {

    CompactCodeAttributeComposer.Label getLabel();

    void addInstruction(BasicInstruction instruction);

    void loadInstructions(SkriptMethodBuilder builder, CompactCodeAttributeComposer code);

    void createLabel(CompactCodeAttributeComposer code);
}