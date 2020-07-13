package software.bigbade.javaskript.compiler.java;

import lombok.RequiredArgsConstructor;
import proguard.classfile.AccessConstants;
import proguard.classfile.ClassConstants;
import proguard.classfile.ProgramClass;
import proguard.classfile.VersionConstants;
import proguard.classfile.editor.ClassBuilder;
import proguard.classfile.io.ProgramClassWriter;
import software.bigbade.javaskript.compiler.utils.SkriptMethodBuilder;
import software.bigbade.javaskript.compiler.utils.Type;
import software.bigbade.javaskript.api.SkriptLineConverter;
import software.bigbade.javaskript.api.objects.SkriptMethod;
import software.bigbade.javaskript.api.variables.SkriptType;
import software.bigbade.javaskript.api.variables.Variables;
import software.bigbade.javaskript.compiler.utils.JarDataStream;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

@RequiredArgsConstructor
public class SkriptToJavaConverter implements SkriptLineConverter {
    private final List<ProgramClass> classes = new ArrayList<>();
    private ClassBuilder main;
    private ClassBuilder classBuilder;
    private SkriptMethodBuilder methodBuilder;

    public SkriptToJavaConverter(String name) {
        main = new ClassBuilder(VersionConstants.CLASS_VERSION_1_8,
                AccessConstants.PUBLIC,
                name,
                ClassConstants.INTERNAL_NAME_JAVA_LANG_OBJECT);
    }

    @Override
    public void startClass(String name, @Nullable String supertype) {
        classBuilder = new ClassBuilder(
                VersionConstants.CLASS_VERSION_1_8,
                AccessConstants.PUBLIC,
                name,
                (supertype == null) ? ClassConstants.INTERNAL_NAME_JAVA_LANG_OBJECT : supertype
        );
        classes.add(classBuilder.getProgramClass());
    }

    @Override
    public void endClass() {
        classBuilder = null;
    }

    @Override
    public void startMethod(String name, Variables variables, @Nullable SkriptType<?> returnType) {
        ClassBuilder parent = (classBuilder == null) ? main : classBuilder;
        methodBuilder = new SkriptMethodBuilder(name, parent.getProgramClass().getName(), returnType, variables);
    }

    @Override
    public void endMethod() {
        int access = AccessConstants.PUBLIC;
        ClassBuilder targetClass;
        if (classBuilder == null) {
            access = access | AccessConstants.STATIC;
            targetClass = main;
        } else {
            targetClass = classBuilder;
        }

        targetClass.addMethod(access, methodBuilder.getName(), methodBuilder.getMethodDescription(), 50, methodBuilder);
        methodBuilder = null;
    }

    @Override
    public <T> void registerVariable(String name, SkriptType<T> variable) {
        if (classBuilder != null) {
            classBuilder.addField(AccessConstants.PUBLIC, name, Type.getDescriptor(variable.getTypeClass()));
        } else {
            main.addField(AccessConstants.PUBLIC | AccessConstants.STATIC, name, Type.getDescriptor(variable.getTypeClass()));
        }
    }

    @Override
    public void callMethod(SkriptMethod method, String line) {

    }

    @Override
    public void writeData(File jar) {
        try (JarOutputStream outputStream = new JarOutputStream(new FileOutputStream(jar))) {
            JarDataStream dataStream = new JarDataStream(outputStream);
            for (ProgramClass programClass : classes) {
                ZipEntry entry = new ZipEntry(programClass.getName());
                outputStream.putNextEntry(entry);
                programClass.accept(new ProgramClassWriter(dataStream));
                outputStream.closeEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
