package software.bigbade.javaskript.compiler.utils;

import software.bigbade.javaskript.api.objects.variable.LocalVariable;
import software.bigbade.javaskript.api.variables.Type;

public final class TypeUtils {
    private TypeUtils() {}

    public static String getMethodDescriptor(LocalVariable[] params, Type outputType) {
        StringBuilder descriptorBuilder = new StringBuilder();
        descriptorBuilder.append("(");
        for (LocalVariable param : params) {
            if(param.getNumber() != 0) {
                param.getType().getDescriptor(descriptorBuilder);
            }
        }
        descriptorBuilder.append(")");
        if(outputType == null) {
            descriptorBuilder.append("V");
        } else {
            outputType.getDescriptor(descriptorBuilder);
        }
        return descriptorBuilder.toString();
    }
}
