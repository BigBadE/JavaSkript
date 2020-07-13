package software.bigbade.javaskript.compiler.statements;

import proguard.classfile.editor.CompactCodeAttributeComposer;
import software.bigbade.javaskript.compiler.instructions.BasicInstruction;
import software.bigbade.javaskript.compiler.instructions.LoadVariableCall;
import software.bigbade.javaskript.compiler.utils.SkriptMethodBuilder;
import software.bigbade.javaskript.compiler.instructions.LocalVariable;
import software.bigbade.javaskript.compiler.java.JavaCodeBlock;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class IfStatement implements JavaCodeBlock {
    private final IfStatementType type;
    private final LocalVariable[] args;

    private final List<JavaCodeBlock> elseIf = new ArrayList<>();
    private int elseIfIndex = 0;
    private JavaCodeBlock start;
    @Nullable
    private JavaCodeBlock end;

    @Override
    public CompactCodeAttributeComposer.Label getLabel() {
        return start.getLabel();
    }

    public IfStatement(IfStatementType type, LocalVariable... args) {
        this.type = type;
        this.args = args;
    }

    @Override
    public void addInstruction(BasicInstruction instruction) {
        if (elseIf.isEmpty()) {
            start.addInstruction(instruction);
        } else if (end == null) {
            elseIf.get(elseIfIndex).addInstruction(instruction);
        } else {
            end.addInstruction(instruction);
        }
    }

    @Override
    public void loadInstructions(SkriptMethodBuilder builder, CompactCodeAttributeComposer code) {
        start.createLabel(code);
        for(LocalVariable localVariable : args) {
            new LoadVariableCall(localVariable).addInstructions(builder, code);
            code.aload(localVariable.getNumber());
        }
        type.inverse().accept(code, start.getLabel());
        start.loadInstructions(builder, code);
        for (JavaCodeBlock block : elseIf) {
            block.loadInstructions(builder, code);
        }
        if (end != null) {
            end.loadInstructions(builder, code);
        }
    }

    @Override
    public void createLabel(CompactCodeAttributeComposer code) {
        throw new IllegalStateException("IfStatements do not create labels in advance.");
    }
}
