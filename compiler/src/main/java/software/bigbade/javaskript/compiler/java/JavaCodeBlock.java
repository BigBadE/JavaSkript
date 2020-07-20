package software.bigbade.javaskript.compiler.java;

import proguard.classfile.editor.CompactCodeAttributeComposer;
import software.bigbade.javaskript.compiler.instructions.BasicInstruction;
import software.bigbade.javaskript.compiler.utils.SkriptMethodBuilder;

import javax.annotation.Nullable;

public interface JavaCodeBlock {
    @Nullable
    JavaCodeBlock getParent();

    void setParent(@Nullable JavaCodeBlock block);

    CompactCodeAttributeComposer.Label getLabel();

    void addInstruction(BasicInstruction instruction);

    void loadInstructions(SkriptMethodBuilder builder, CompactCodeAttributeComposer code);

    CompactCodeAttributeComposer.Label createLabel(CompactCodeAttributeComposer code);
}