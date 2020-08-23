package software.bigbade.javaskript.compiler.statements;

import lombok.Getter;
import lombok.Setter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import software.bigbade.javaskript.api.objects.variable.LocalVariable;
import software.bigbade.javaskript.compiler.instructions.BasicInstruction;
import software.bigbade.javaskript.compiler.instructions.LoadVariableCall;
import software.bigbade.javaskript.compiler.java.JavaCodeBlock;
import software.bigbade.javaskript.compiler.utils.SkriptMethodBuilder;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class IfStatement implements JavaCodeBlock {
    @Getter
    @Setter
    @Nullable
    private JavaCodeBlock parent;

    private final IfStatementType type;
    private final LocalVariable<?>[] args;

    private final List<IfStatement> elseIf = new ArrayList<>();
    @Setter
    private JavaCodeBlock start;
    @Nullable
    @Setter
    private JavaCodeBlock end;

    @Override
    public Label getLabel() {
        return start.getLabel();
    }

    public IfStatement(IfStatementType type, LocalVariable<?>... args) {
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
    public void loadInstructions(SkriptMethodBuilder<?> builder, MethodVisitor visitor) {
        start.createLabel(visitor);
        for(LocalVariable<?> localVariable : args) {
            new LoadVariableCall(localVariable).addInstructions(builder, visitor);
        }
        for(JavaCodeBlock block : elseIf) {
            block.createLabel(visitor);
        }
        Label endLabel;
        if(end != null) {
            endLabel = end.createLabel(visitor);
        } else {
            endLabel = new Label();
        }
        type.inverse().accept(visitor, endLabel);
        start.loadInstructions(builder, visitor);
        for (JavaCodeBlock block : elseIf) {
            block.loadInstructions(builder, visitor);
        }
        if (end != null) {
            end.loadInstructions(builder, visitor);
        }
    }

    @Override
    public Label createLabel(MethodVisitor code) {
        return start.createLabel(code);
    }
}
