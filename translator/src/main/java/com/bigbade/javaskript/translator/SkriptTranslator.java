package com.bigbade.javaskript.translator;

import com.bigbade.javaskript.api.java.defs.IMethodDef;
import com.bigbade.javaskript.api.java.defs.IPackageDef;
import com.bigbade.javaskript.api.skript.defs.IParsingDef;
import com.bigbade.javaskript.api.skript.defs.ISkriptFile;
import com.bigbade.javaskript.translator.impl.JavaPackage;
import com.bigbade.javaskript.translator.shading.ShadeManager;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.regex.Pattern;

public class SkriptTranslator {
    private static final Pattern INTERNAL_PACKAGE_PATTERN = Pattern.compile(File.separator);

    public IPackageDef writeCode(ISkriptFile file, JarOutputStream output) throws IOException {
        IPackageDef basePackage = new JavaPackage("com.bigbade.generated." + file.getFileName());
        ShadeManager shadeManager = new ShadeManager();
        for(IParsingDef def : file.getParsedFunctions()) {
            IMethodDef target = def.locateMethod(basePackage);
            String targetPath = target.getJavaClass().getJavaFile().getFilePath();
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS + ClassWriter.COMPUTE_FRAMES);
            writer.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC,
                    INTERNAL_PACKAGE_PATTERN.matcher(targetPath).replaceAll("/"),
                    null, null, new String[0]);

            //TODO shade the parsing def
            //shadeManager.shadeMethod(writer, target, def.getFunctionDef().getClass());

            output.putNextEntry(new JarEntry(targetPath));
            output.write(writer.toByteArray());
        }

        return basePackage;
    }
}
