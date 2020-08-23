package software.bigbade.javaskript.compiler.utils;

import software.bigbade.javaskript.api.variables.SkriptType;
import software.bigbade.javaskript.api.variables.Type;

public final class TypeUtils {
    private TypeUtils() {
    }

    public static String getMethodDescriptor(SkriptType[] params, Type outputType, boolean ignoreFirst) {
        StringBuilder descriptorBuilder = new StringBuilder();
        descriptorBuilder.append("(");
        for (SkriptType param : params) {
            if(ignoreFirst) {
                ignoreFirst = false;
                continue;
            }
            param.getType().getDescriptor(descriptorBuilder);
        }
        descriptorBuilder.append(")");
        if (outputType == null) {
            descriptorBuilder.append("V");
        } else {
            outputType.getDescriptor(descriptorBuilder);
        }
        return descriptorBuilder.toString();
    }
}
