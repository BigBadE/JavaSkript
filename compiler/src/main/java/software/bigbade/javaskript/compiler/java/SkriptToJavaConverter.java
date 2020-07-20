package software.bigbade.javaskript.compiler.java;

import lombok.RequiredArgsConstructor;
import proguard.classfile.AccessConstants;
import proguard.classfile.ClassConstants;
import proguard.classfile.ProgramClass;
import proguard.classfile.VersionConstants;
import proguard.classfile.editor.ClassBuilder;
import proguard.classfile.io.ProgramClassWriter;
import software.bigbade.javaskript.api.SkriptLineConverter;
import software.bigbade.javaskript.api.instructions.Statements;
import software.bigbade.javaskript.api.instructions.VariableChanges;
import software.bigbade.javaskript.api.objects.LocalVariable;
import software.bigbade.javaskript.api.objects.ParsedSkriptMethod;
import software.bigbade.javaskript.api.variables.SkriptType;
import software.bigbade.javaskript.api.variables.Type;
import software.bigbade.javaskript.api.variables.Variables;
import software.bigbade.javaskript.compiler.instructions.ConvertVariableCall;
import software.bigbade.javaskript.compiler.instructions.CreateObjectCall;
import software.bigbade.javaskript.compiler.instructions.MathCall;
import software.bigbade.javaskript.compiler.instructions.PushVariableCall;
import software.bigbade.javaskript.compiler.instructions.SetVariableCall;
import software.bigbade.javaskript.compiler.utils.JarDataStream;
import software.bigbade.javaskript.compiler.utils.SkriptMethodBuilder;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

@RequiredArgsConstructor
public class SkriptToJavaConverter implements SkriptLineConverter {
    private final List<ProgramClass> classes = new ArrayList<>();
    private Map<String, LocalVariable> classVariables = new HashMap<>();
    private ClassBuilder main;
    private ClassBuilder classBuilder;
    private SkriptMethodBuilder methodBuilder;

    public SkriptToJavaConverter(String name, @Nullable String superclass) {
        main = new ClassBuilder(VersionConstants.CLASS_VERSION_1_8,
                AccessConstants.PUBLIC,
                name + ".class",
                (superclass == null) ? ClassConstants.NAME_JAVA_LANG_OBJECT : superclass);
    }

    @Override
    public void startClass(String name, @Nullable String supertype) {
        classBuilder = new ClassBuilder(
                VersionConstants.CLASS_VERSION_1_8,
                AccessConstants.PUBLIC,
                name,
                (supertype == null) ? ClassConstants.NAME_JAVA_LANG_OBJECT : supertype
        );
        classes.add(classBuilder.getProgramClass());
        classVariables = new HashMap<>();
    }

    @Override
    public void endClass() {
        classBuilder = null;
    }

    @Override
    public void startMethod(String name, Variables variables, @Nullable SkriptType<?> returnType) {
        ClassBuilder parent = (classBuilder == null) ? main : classBuilder;
        methodBuilder = new SkriptMethodBuilder(name, parent.getProgramClass().getName(), returnType, classVariables, variables);
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
        if(methodBuilder != null) {
            methodBuilder.addLocalVariable(name, variable.getTypeClass());
        } else {
            if (classBuilder != null) {
                classBuilder.addField(AccessConstants.PUBLIC, name, Type.getDescriptor(variable.getTypeClass()));
            } else {
                main.addField(AccessConstants.PUBLIC | AccessConstants.STATIC, name, Type.getDescriptor(variable.getTypeClass()));
            }
        }
    }

    /**
     * Calls a SkriptMethod
     *
     * @param method Method to call
     */
    @Override
    public void callMethod(ParsedSkriptMethod method) {
        if (method.getMethod().isConstructor()) {
            methodBuilder.addInstruction(new CreateObjectCall<>(method.getMethod().getOwner(), method.getName(), method.getVariables().toArray(new LocalVariable[0])));
        }
    }


    @Override
    public void manipulateVariable(VariableChanges change, String first, String second) {
        LocalVariable firstVariable = getLocalVariable(first);
        LocalVariable secondVariable = getLocalVariable(second);
        switch (change) {
            case MULTIPLY:
                methodBuilder.addInstruction(new MathCall(change, firstVariable, secondVariable));
        }
    }

    @Override
    public void convertVariable(String converting, Class<?> type) {
        methodBuilder.addInstruction(new ConvertVariableCall<>(getLocalVariable(converting), type));
    }

    @Override
    public <T> void setVariable(String variable, T value) {
        methodBuilder.addInstruction(new PushVariableCall<>(value));
        methodBuilder.addInstruction(new SetVariableCall(getLocalVariable(variable)));
    }

    @Override
    public void addJavaBlock(Statements statements) {
        switch(statements) {
            case CODE_BLOCK:
                methodBuilder.addJavaBlock(new BasicJavaCodeBlock());
        }
    }

    @Override
    public void writeData(File jar) {
        classes.add(main.getProgramClass());
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

    @Override
    public LocalVariable getLocalVariable(String name) {
        if(classVariables.containsKey(name)) {
            return classVariables.get(name);
        } else if(methodBuilder != null) {
            return methodBuilder.getLocalVariable(name);
        }
        throw new NullPointerException("Tried to get local variable that doesn't exist");
    }
}
