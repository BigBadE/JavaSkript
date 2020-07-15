package software.bigbade.javaskript.compiler.utils;

import software.bigbade.javaskript.api.objects.LocalVariable;
import software.bigbade.javaskript.api.variables.Type;

public final class TypeUtils {
    private TypeUtils() {}

    public static String getMethodDescriptor(LocalVariable[] params, Class<?> outputType) {
        StringBuilder descriptorBuilder = new StringBuilder();
        descriptorBuilder.append("(");
        for (LocalVariable param : params) {
            param.getType().getDescriptor(descriptorBuilder);
        }
        descriptorBuilder.append(")");
        if(outputType == null) {
            descriptorBuilder.append("V");
        } else {
            Type.getType(outputType).getDescriptor(descriptorBuilder);
        }
        return descriptorBuilder.toString();
    }
}
