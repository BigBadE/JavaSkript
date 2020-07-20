package software.bigbade.javaskript.compiler.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import proguard.classfile.editor.ClassBuilder;
import proguard.classfile.editor.CompactCodeAttributeComposer;
import software.bigbade.javaskript.api.objects.LocalVariable;
import software.bigbade.javaskript.api.variables.SkriptType;
import software.bigbade.javaskript.api.variables.Type;
import software.bigbade.javaskript.api.variables.Variables;
import software.bigbade.javaskript.compiler.instructions.BasicInstruction;
import software.bigbade.javaskript.compiler.java.JavaCodeBlock;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class SkriptMethodBuilder implements ClassBuilder.CodeBuilder {
    @Getter
    private final String name;
    @Nullable
    private final SkriptType<?> returnType;

    @Getter
    private final String parent;

    private final Map<String, LocalVariable> classVariables;
    private final Map<String, LocalVariable> methodVariables = new HashMap<>();
    private final List<JavaCodeBlock> codeBlocks = new ArrayList<>();

    private JavaCodeBlock currentBlock = null;

    public SkriptMethodBuilder(String name, String parent, @Nullable SkriptType<?> returnType, Map<String, LocalVariable> classVariables, Variables variables) {
        this.name = name;
        this.returnType = returnType;
        this.parent = parent;
        this.classVariables = classVariables;
        addLocalVariable("this", Type.getType(parent));
        for(Map.Entry<String, SkriptType<?>> param : variables.getAllVariables().entrySet()) {
            addLocalVariable(param.getKey(), param.getValue().getTypeClass());
        }
    }

    public String getMethodDescription() {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        for (LocalVariable variable : methodVariables.values()) {
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

    public void addJavaBlock(JavaCodeBlock block) {
        if(currentBlock != null) {
            codeBlocks.add(currentBlock);
        }
        block.setParent(currentBlock);
        currentBlock = block;
    }
    public void addLocalVariable(String name, Class<?> clazz) {
        methodVariables.put(name, new LocalVariable(classVariables.size()+methodVariables.size()-2, clazz));
    }

    public void addLocalVariable(String name, Type type) {
        methodVariables.put(name, new LocalVariable(classVariables.size()+methodVariables.size()-2, type));
    }

    public LocalVariable getLocalVariable(String name) {
        return methodVariables.get(name);
    }

    @Override
    public void compose(CompactCodeAttributeComposer code) {
        for(JavaCodeBlock block : codeBlocks) {
            block.loadInstructions(this, code);
        }
    }
}
