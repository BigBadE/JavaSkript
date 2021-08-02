package com.bigbade.javaskript.translator;

import com.bigbade.javaskript.api.java.defs.IMethodDef;
import com.bigbade.javaskript.api.java.defs.IPackageDef;
import com.bigbade.javaskript.api.skript.defs.IParsingDef;
import com.bigbade.javaskript.api.skript.defs.ISkriptFile;
import com.bigbade.javaskript.translator.impl.JavaPackage;
import com.bigbade.javaskript.translator.shading.ShadeManager;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.regex.Pattern;

public class SkriptTranslator {
    public static final Logger LOGGER = LoggerFactory.getLogger(SkriptTranslator.class);
    private static final Pattern INTERNAL_PACKAGE_PATTERN = Pattern.compile(File.separator);

    public IPackageDef writeCode(ISkriptFile file, JarOutputStream output) throws IOException {
        IPackageDef basePackage = new JavaPackage("com.bigbade.generated." + file.getFileName());
        for(IParsingDef def : file.getParsedFunctions()) {
            IMethodDef target = def.locateMethod(basePackage);
            String targetPath = target.getJavaClass().getClassName();
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            writer.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC,
                    INTERNAL_PACKAGE_PATTERN.matcher(targetPath).replaceAll("/"),
                    null, null, new String[0]);
            //TODO shade the parsing def
            ShadeManager.shadeMethod(writer.visitMethod(target.getModifiers().getMask() & 7,
                            target.getMethodName(), getDescriptor(target), /*TODO*/ null, /*TODO*/ null),
                    target, def.getFunctionDef());

            output.putNextEntry(new JarEntry(targetPath));
            output.write(writer.toByteArray());
        }

        return basePackage;
    }

    public static String getDescriptor(IMethodDef target) {
        Type[] params = new Type[target.getParameters().size()];
        for(int i = 0; i < params.length; i++) {
            params[i] = Type.getType(target.getParameters().get(i).getType().getInternalName());
        }
        return Type.getMethodDescriptor(Type.getType(target.getReturnType().getInternalName()), params);
    }
}
