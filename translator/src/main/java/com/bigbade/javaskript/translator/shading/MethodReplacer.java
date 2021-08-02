package com.bigbade.javaskript.translator.shading;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

@RequiredArgsConstructor
@Getter
public abstract class MethodReplacer {
    private final String name;
    private final String descriptor;

    @SuppressWarnings("SameParameterValue")
    @SneakyThrows(NoSuchMethodException.class)
    public static String getNamedMethod(Class<?> target, String name, Class<?>... args) {
        return Type.getMethodDescriptor(target.getDeclaredMethod(name, args));
    }

    public abstract int replace(MethodVisitor writer, Object lastLoaded, int offset);
}
