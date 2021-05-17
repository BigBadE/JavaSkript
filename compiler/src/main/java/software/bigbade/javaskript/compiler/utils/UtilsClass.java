package software.bigbade.javaskript.compiler.utils;

import lombok.Getter;
import software.bigbade.javaskript.api.factory.LineConverterFactory;
import software.bigbade.javaskript.api.objects.SkriptLineConverter;

public final class UtilsClass {
    @Getter
    private static SkriptLineConverter utilsMethodBuilder;

    private UtilsClass() {}

    public static void setup() {
        if(utilsMethodBuilder != null) {
            throw new IllegalStateException("Trying to setup already-setup utils builder!");
        }
        utilsMethodBuilder = LineConverterFactory.getLineConverter("Utils", new String[0], null);
    }
}
