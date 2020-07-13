package software.bigbade.javaskript.compiler.instructions;

import proguard.classfile.editor.CompactCodeAttributeComposer;
import software.bigbade.javaskript.compiler.utils.SkriptMethodBuilder;

public interface BasicInstruction {

    void addInstructions(SkriptMethodBuilder builder, CompactCodeAttributeComposer code);
}
