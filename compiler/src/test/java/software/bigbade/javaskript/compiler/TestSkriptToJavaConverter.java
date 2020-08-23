package software.bigbade.javaskript.compiler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.CheckClassAdapter;
import software.bigbade.javaskript.api.instructions.Statements;
import software.bigbade.javaskript.api.instructions.VariableChanges;
import software.bigbade.javaskript.api.objects.MethodLineConverter;
import software.bigbade.javaskript.api.objects.ParsedSkriptMethod;
import software.bigbade.javaskript.api.objects.SkriptMethod;
import software.bigbade.javaskript.api.objects.variable.LocalVariable;
import software.bigbade.javaskript.api.variables.ClassType;
import software.bigbade.javaskript.api.variables.SkriptTypes;
import software.bigbade.javaskript.api.variables.Variables;
import software.bigbade.javaskript.compiler.java.BasicJavaClass;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

public class TestSkriptToJavaConverter {
    @TempDir
    public static File temporaryFolder;

    private final File outputFile = new File(temporaryFolder, "test.jar");

    //@Test
    public void testASM() {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        writer.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, "Main", null, Object.class.getName().replace(".", "/"), null);
        MethodVisitor methodVisitor = writer.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
        methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        methodVisitor.visitInsn(Opcodes.RETURN);
        methodVisitor.visitMaxs(0, 0);
        methodVisitor.visitEnd();
        methodVisitor = writer.visitMethod(Opcodes.ACC_PUBLIC, "test", "(I)I", null, null);
        methodVisitor.visitParameter("testValue", Opcodes.ACC_MANDATED);
        Label label = new Label();
        methodVisitor.visitLabel(label);
        methodVisitor.visitLdcInsn(6);
        methodVisitor.visitVarInsn(Opcodes.ILOAD, 1);
        methodVisitor.visitInsn(Opcodes.IMUL);
        methodVisitor.visitInsn(Opcodes.IRETURN);
        methodVisitor.visitMaxs(0, 0);
        methodVisitor.visitEnd();

        try (JarOutputStream outputStream = new JarOutputStream(new FileOutputStream(outputFile))) {
            ZipEntry entry = new ZipEntry("Main.class");
            outputStream.putNextEntry(entry);
            outputStream.write(writer.toByteArray());
            outputStream.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (JarFile file = new JarFile(outputFile);
             JarInputStream jarInputStream = new JarInputStream(new FileInputStream(outputFile))) {
            checkBytecode(file.getInputStream(jarInputStream.getNextJarEntry()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Class<?> clazz = loadClass("Main", writer.toByteArray());
            Assertions.assertEquals(clazz
                    .getDeclaredMethod("test", Integer.TYPE)
                    .invoke(clazz.newInstance(), 7), 7 * 6);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 0: ldc           #8                  // int 6
     * 2: iload_2
     * 3: imul
     * 4: return
     */
    @DisplayName("Successfully build and loaded jar")
    @Test
    public void testSkriptToJavaConverter() {
        BasicJavaClass converter = new BasicJavaClass("Main", new String[0], null);
        Variables variables = new Variables();
        MethodLineConverter<?> methodConverter = converter.startMethod("<init>", variables, null);
        methodConverter.addJavaBlock(Statements.CODE_BLOCK);
        ParsedSkriptMethod method = new ParsedSkriptMethod(new SkriptMethod() {
            @Nullable
            @Override
            public ParsedSkriptMethod parse(String line) {
                throw new IllegalStateException("Already parsed!");
            }

            @Override
            public Variables getVariables() {
                return new Variables();
            }

            @Override
            public Class<?> getOwner() {
                return Object.class;
            }

            @Override
            public String getName() {
                return "<init>";
            }

            @Override
            public boolean isConstructor() {
                return false;
            }

            @Override
            public boolean isStatic() {
                return false;
            }
        }, null);
        method.addLocalVariable(new ClassType<>("Main"));
        methodConverter.loadVariable(methodConverter.getLocalVariable("this"))
                .callMethod(method)
                .returnNothing();
        converter.endMethod(methodConverter);
        variables = new Variables();
        variables.addVariable("testNumb", SkriptTypes.INTEGER);
        methodConverter = converter.startMethod("test", variables, SkriptTypes.INTEGER)
                .addJavaBlock(Statements.CODE_BLOCK);
        LocalVariable<Integer> output = methodConverter.getLocalVariable("testNumb");
        methodConverter.loadVariable(output);
        methodConverter.loadConstant(6);
        methodConverter = methodConverter.manipulateVariable(VariableChanges.MULTIPLY, SkriptTypes.INTEGER, SkriptTypes.INTEGER);
        methodConverter.returnVariable(SkriptTypes.INTEGER);
        converter.endMethod(methodConverter);
        converter.endClass();
        try (JarOutputStream outputStream = new JarOutputStream(new FileOutputStream(outputFile))) {
            converter.writeData(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (JarFile file = new JarFile(outputFile);
             JarInputStream jarInputStream = new JarInputStream(new FileInputStream(outputFile))) {
            checkBytecode(file.getInputStream(jarInputStream.getNextJarEntry()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Class<?> clazz = loadClass("Main", converter.getData());
            Assertions.assertEquals(clazz
                    .getDeclaredMethod("test", Integer.TYPE)
                    .invoke(clazz.newInstance(), 7), 7 * 6);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    private void checkBytecode(InputStream stream) throws IOException {
        ClassReader classReader = new ClassReader(stream);
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
        ClassVisitor classVisitor = new CheckClassAdapter(classWriter, true);
        classReader.accept(classVisitor, 0);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        CheckClassAdapter.verify(new ClassReader(classWriter.toByteArray()), false, printWriter);
        Assertions.assertTrue(stringWriter.toString().isEmpty());
    }

    private Class<?> loadClass(String name, byte[] b) {
        // Override defineClass (as it is protected) and define the class.
        Class<?> clazz = null;
        try {
            ClassLoader loader = getClass().getClassLoader();
            Class<?> cls = Class.forName("java.lang.ClassLoader");
            java.lang.reflect.Method method =
                    cls.getDeclaredMethod(
                            "defineClass",
                            String.class, byte[].class, int.class, int.class);

            // Protected method invocation.
            method.setAccessible(true);
            try {
                Object[] args =
                        new Object[]{name, b, 0, b.length};
                clazz = (Class<?>) method.invoke(loader, args);
            } finally {
                method.setAccessible(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return clazz;
    }
}
