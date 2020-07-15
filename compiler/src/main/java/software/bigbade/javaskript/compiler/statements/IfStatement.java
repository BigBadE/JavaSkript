package software.bigbade.javaskript.compiler.statements;

import lombok.Getter;
import lombok.Setter;
import proguard.classfile.editor.CompactCodeAttributeComposer;
import software.bigbade.javaskript.compiler.instructions.BasicInstruction;
import software.bigbade.javaskript.compiler.instructions.LoadVariableCall;
import software.bigbade.javaskript.compiler.utils.SkriptMethodBuilder;
import software.bigbade.javaskript.api.objects.LocalVariable;
import software.bigbade.javaskript.compiler.java.JavaCodeBlock;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class IfStatement implements JavaCodeBlock {
    @Getter
    @Nullable
    private final JavaCodeBlock parent;
    private final IfStatementType type;
    private final LocalVariable[] args;

    private final List<IfStatement> elseIf = new ArrayList<>();
    @Setter
    private JavaCodeBlock start;
    @Nullable
    @Setter
    private JavaCodeBlock end;

    @Override
    public CompactCodeAttributeComposer.Label getLabel() {
        return start.getLabel();
    }

    public IfStatement(@Nullable JavaCodeBlock parent, IfStatementType type, LocalVariable... args) {
        this.parent = parent;
        this.type = type;
        this.args = args;
    }

    public void addElseIf(IfStatement statement) {
        elseIf.add(statement);
    }

    @Override
    public void addInstruction(BasicInstruction instruction) {
        if (end == null) {
            start.addInstruction(instruction);
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
        for(JavaCodeBlock block : elseIf) {
            block.createLabel(code);
        }
        CompactCodeAttributeComposer.Label endLabel;
        if(end != null) {
            endLabel = end.createLabel(code);
        } else {
            endLabel = code.createLabel();
        }
        type.inverse().accept(code, endLabel);
        start.loadInstructions(builder, code);
        for (JavaCodeBlock block : elseIf) {
            block.loadInstructions(builder, code);
        }
        if (end != null) {
            end.loadInstructions(builder, code);
        }
    }

    @Override
    public CompactCodeAttributeComposer.Label createLabel(CompactCodeAttributeComposer code) {
        return start.createLabel(code);
    }
}
