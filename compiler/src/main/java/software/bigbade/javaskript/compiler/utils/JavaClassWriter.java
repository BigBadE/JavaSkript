package software.bigbade.javaskript.compiler.utils;

import lombok.Getter;
import org.objectweb.asm.ClassWriter;
import software.bigbade.javaskript.api.objects.variable.LocalVariable;

import java.util.HashMap;
import java.util.Map;

public class JavaClassWriter extends ClassWriter {
    @Getter
    private final String name;

    @Getter
    private final Map<String, LocalVariable<?>> classVariables = new HashMap<>();

    public JavaClassWriter(String name, int flags) {
        super(flags);
        this.name = name;
    }
}
