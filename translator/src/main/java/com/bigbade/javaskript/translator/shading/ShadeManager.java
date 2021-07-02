package com.bigbade.javaskript.translator.shading;

import com.bigbade.javaskript.api.java.defs.IMethodDef;
import com.bigbade.javaskript.api.skript.code.ISkriptInstruction;
import org.objectweb.asm.ClassWriter;

public class ShadeManager {
    public void shadeMethod(ClassWriter writer, IMethodDef target, ISkriptInstruction instruction) {
        //TODO shade method, handling branch instructions
    }
}
