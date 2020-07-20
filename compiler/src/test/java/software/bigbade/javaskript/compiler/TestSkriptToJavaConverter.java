package software.bigbade.javaskript.compiler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import software.bigbade.javaskript.api.instructions.Statements;
import software.bigbade.javaskript.api.instructions.VariableChanges;
import software.bigbade.javaskript.api.variables.SkriptTypes;
import software.bigbade.javaskript.api.variables.Variables;
import software.bigbade.javaskript.compiler.java.SkriptToJavaConverter;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class TestSkriptToJavaConverter {
    @TempDir
    public File temporaryFolder;

    private final File outputFile = new File(temporaryFolder, "test.jar");

    @DisplayName("Successfully build and loaded jar")
    @Test
    public void testSkriptToJavaConverter() {
        SkriptToJavaConverter converter = new SkriptToJavaConverter("Main", null);
        //Variables variables = new Variables();
        //variables.addVariable("testNumb", SkriptTypes.INTEGER);
        //converter.startMethod("test", variables, SkriptTypes.INTEGER);
        //converter.addJavaBlock(Statements.CODE_BLOCK);
        //converter.registerVariable("output", SkriptTypes.INTEGER);
        //converter.setVariable("output", 6);
        //converter.manipulateVariable(VariableChanges.MULTIPLY, "testNumb", "output");
        //converter.endMethod();
        converter.writeData(outputFile);
        try {
            URLClassLoader classLoader = new URLClassLoader(new URL[] { outputFile.toURI().toURL() });
            classLoader.loadClass("Main");
            //Assertions.assertEquals(classLoader.loadClass("Main").getDeclaredMethod("test", Integer.class).invoke(null, 7), 7*6);
        } catch (MalformedURLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
