package com.bigbade.javaskript.translator.shading;

import com.bigbade.javaskript.api.java.defs.IMethodDef;
import com.bigbade.javaskript.api.skript.addon.ISkriptFunctionDef;
import com.bigbade.javaskript.api.skript.code.IParsedInstruction;
import com.bigbade.javaskript.api.skript.defs.ICodeDef;
import com.bigbade.javaskript.api.skript.defs.ICodeTranslator;
import com.bigbade.javaskript.api.skript.defs.IValueTranslator;
import com.bigbade.javaskript.translator.SkriptTranslator;
import lombok.Getter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class ShadeManager {
    private ShadeManager() {}

    public static void shadeMethod(MethodVisitor writer, IMethodDef target, ISkriptFunctionDef instruction) throws IOException {
        ClassReader reader = new ClassReader(instruction.getClass().getName());

        //Offset is 0 if it's static or 1 if it's an instance method
        reader.accept(new TargetMethodVisitor(writer, instruction, target.getMethodName(),
                        SkriptTranslator.getDescriptor(target), ~(target.getModifiers().getMask() & Modifier.STATIC)),
                ClassReader.SKIP_DEBUG);
    }

    public static int shadeInstruction(MethodVisitor writer, IParsedInstruction instruction, int offset) throws IOException {
        ClassReader reader = new ClassReader(instruction.getClass().getName());
        Method target = instruction.getInstruction().getMethod();

        //Offset is 0 if it's static or -1 if it's an instance method
        TargetMethodVisitor methodVisitor =
                new TargetMethodVisitor(writer, null, target.getName(), Type.getMethodDescriptor(target),
                        offset - ~(target.getModifiers() & Modifier.STATIC));
        reader.accept(methodVisitor,
                ClassReader.SKIP_DEBUG);
        return methodVisitor.getMethodShader().getOffset();
    }
}

class TargetMethodVisitor extends ClassVisitor {
    private final String name;
    private final String descriptor;

    @Getter
    private final MethodShader methodShader;

    public TargetMethodVisitor(MethodVisitor writer, ISkriptFunctionDef functionDef, String name, String descriptor, int offset) {
        super(Opcodes.ASM9);
        this.methodShader = new MethodShader(writer, offset, functionDef == null ? new MethodReplacer[0] :
                new MethodReplacer[] { new ExecuteReplacer(functionDef) });
        this.name = name;
        this.descriptor = descriptor;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if(!this.name.equals(name) || !this.descriptor.equals(descriptor)) {
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }

        return methodShader;
    }
}

class MethodShader extends MethodVisitor {
    private final MethodReplacer[] replacers;

    @Getter
    private int offset;
    private Object lastLoaded;

    public MethodShader(MethodVisitor writer, int offset, MethodReplacer... replacers) {
        super(Opcodes.ASM9, writer);
        this.replacers = replacers;
        this.offset = offset;
    }

    @Override
    public void visitInsn(int opcode) {
        //Increment the local variable offset
        if(opcode == Opcodes.ASTORE || opcode == Opcodes.FSTORE || opcode == Opcodes.ISTORE) {
            offset++;
        } else if(opcode == Opcodes.DSTORE || opcode == Opcodes.LSTORE) {
            offset += 2;
        }
        super.visitInsn(opcode);
    }

    @Override
    public void visitLdcInsn(Object value) {
        lastLoaded = value;
        super.visitLdcInsn(value);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        for(MethodReplacer replacer : replacers) {
            if(owner.equals(replacer.getName()) && descriptor.equals(replacer.getDescriptor())) {
                offset += replacer.replace(mv, lastLoaded, offset);
                return;
            }
        }

        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        super.visitLocalVariable(name, descriptor, signature, start, end, index+offset);
    }

    @Override
    public void visitVarInsn(int opcode, int variable) {
        super.visitVarInsn(opcode, variable+offset);
    }
}

class ExecuteReplacer extends MethodReplacer {
    private static final String EXECUTE_DESCRIPTOR = getNamedMethod(ICodeDef.class, "execute");

    private final ISkriptFunctionDef functionDef;

    public ExecuteReplacer(ISkriptFunctionDef functionDef) {
        super("execute", EXECUTE_DESCRIPTOR);
        this.functionDef = functionDef;
    }

    @Override
    public int replace(MethodVisitor writer, Object lastLoaded, int offset) {
        IValueTranslator<?> translator = functionDef.getTranslators().get(lastLoaded == null ? null : lastLoaded.toString());
        if(translator == null) {
            throw new IllegalStateException("Unknown code def " + lastLoaded + ", make sure you pass a string to execute like " +
                    "\"execute(\"mykey\")\"");
        } else if(!(translator instanceof ICodeTranslator)) {
            throw new IllegalStateException("Code def " + lastLoaded + "isn't a code translator, " +
                    "make sure you pass a string to execute like \"execute(\"mykey\")\"");
        }
        ICodeDef codeDef = ((ICodeTranslator) translator).getValue();
        try {
            for (IParsedInstruction instruction : codeDef.getInstructions()) {
                offset += ShadeManager.shadeInstruction(writer, instruction, offset);
            }
        } catch (IOException e) {
            SkriptTranslator.LOGGER.error("Error reading instruction", e);
        }
        return offset;
    }
}
