package software.bigbade.javaskript.compiler.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import proguard.classfile.editor.ClassBuilder;
import proguard.classfile.editor.CompactCodeAttributeComposer;
import software.bigbade.javaskript.api.variables.Type;
import software.bigbade.javaskript.compiler.instructions.BasicInstruction;
import software.bigbade.javaskript.api.variables.SkriptType;
import software.bigbade.javaskript.api.variables.Variables;
import software.bigbade.javaskript.api.objects.LocalVariable;
import software.bigbade.javaskript.compiler.java.JavaCodeBlock;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class SkriptMethodBuilder implements ClassBuilder.CodeBuilder {
    @Getter
    private final String name;
    @Nullable
    private final SkriptType<?> returnType;

    @Getter
    @Setter
    private int localVariables;

    @Getter
    private final LocalVariable thisVariable;
    @Getter
    private final String parent;

    private final List<LocalVariable> params = new ArrayList<>();
    private final List<JavaCodeBlock> codeBlocks = new ArrayList<>();

    private JavaCodeBlock currentBlock = null;

    public SkriptMethodBuilder(String name, String parent, @Nullable SkriptType<?> returnType, Variables variables) {
        this.name = name;
        this.returnType = returnType;
        this.parent = parent;
        thisVariable = new LocalVariable(0, Type.getType(parent), "this");
        localVariables = 1;
        for(Map.Entry<String, SkriptType<?>> param : variables.getAllVariables().entrySet()) {
            params.add(new LocalVariable(localVariables++, param.getValue().getTypeClass(), param.getKey()));
        }
    }

    public String getMethodDescription() {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        for (LocalVariable variable : params) {
            variable.getType().getDescriptor(builder);
        }
        builder.append(")");
        if(returnType == null) {
            builder.append("V");
        } else {
            Type.getType(returnType.getTypeClass()).getDescriptor(builder);
        }
        return builder.toString();
    }

    public void addInstruction(BasicInstruction instruction) {
        currentBlock.addInstruction(instruction);
    }

    @Override
    public void compose(CompactCodeAttributeComposer code) {
        for(JavaCodeBlock block : codeBlocks) {
            block.loadInstructions(this, code);
        }
    }
}
