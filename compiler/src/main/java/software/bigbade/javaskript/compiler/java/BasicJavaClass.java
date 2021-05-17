package software.bigbade.javaskript.compiler.java;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import software.bigbade.javaskript.api.objects.MethodLineConverter;
import software.bigbade.javaskript.api.objects.SkriptLineConverter;
import software.bigbade.javaskript.api.objects.variable.LocalVariable;
import software.bigbade.javaskript.api.variables.ClassType;
import software.bigbade.javaskript.api.variables.SkriptType;
import software.bigbade.javaskript.api.variables.Variables;
import software.bigbade.javaskript.compiler.utils.JavaClassWriter;
import software.bigbade.javaskript.compiler.utils.SkriptMethodBuilder;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

@RequiredArgsConstructor
public class BasicJavaClass implements SkriptLineConverter {
    @Getter
    private JavaClassWriter classBuilder;

    public BasicJavaClass(String name, String[] interfaces, @Nullable String superclass) {
        startClass(name, interfaces, superclass);
    }

    private static String getInternal(String className) {
        return className.replace(".", "/");
    }

    private void startClass(String name, String[] interfaces, @Nullable String superclass) {
        classBuilder = new JavaClassWriter(name, ClassWriter.COMPUTE_FRAMES);
        for(int i = 0; i < interfaces.length; i++) {
            interfaces[i] = BasicJavaClass.getInternal(interfaces[i]);
        }
        if(superclass == null) {
            superclass = Object.class.getName();
        }
        superclass = BasicJavaClass.getInternal(superclass);
        classBuilder.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, name, null, superclass, interfaces);
    }

    public void endClass() {
        classBuilder.visitEnd();
    }

    public MethodLineConverter<Void> startMethod(String name, Variables variables, @Nullable SkriptType<?> returnType) {
        variables.addVariable("this", new ClassType<>(classBuilder.getName()));
        return new SkriptMethodBuilder<>(classBuilder, name, returnType, variables);
    }

    public void endMethod(MethodLineConverter<?> converter) {
        ((SkriptMethodBuilder<?>) converter).compose(classBuilder.visitMethod(Opcodes.ACC_PUBLIC, converter.getName(), converter.getMethodDescription(), null, null));
    }

    public void registerVariable(String name, SkriptType<?> variable) {
        classBuilder.visitField(Opcodes.ACC_PUBLIC, name, variable.getType().getDescriptor(), null, null);
    }

    public byte[] getData() {
        return classBuilder.toByteArray();
    }

    @Override
    public void writeData(JarOutputStream outputStream) throws IOException {
        JarEntry entry = new JarEntry(classBuilder.getName() + ".class");
        outputStream.putNextEntry(entry);
        outputStream.write(getData());
        outputStream.closeEntry();
    }

    @SuppressWarnings("unchecked")
    public <C> LocalVariable<C> getLocalVariable(String name) {
        return (LocalVariable<C>) classBuilder.getClassVariables().get(name);
    }
}
