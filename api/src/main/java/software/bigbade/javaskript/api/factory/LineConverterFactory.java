package software.bigbade.javaskript.api.factory;

import lombok.Setter;
import software.bigbade.javaskript.api.objects.SkriptLineConverter;

import javax.annotation.Nullable;

public abstract class LineConverterFactory {
    @Setter
    private static LineConverterFactory factory;

    public static SkriptLineConverter getLineConverter(String name, String[] interfaces, @Nullable String superclass) {
        return factory.createLineConverter(name, interfaces, superclass);
    }

    public abstract SkriptLineConverter createLineConverter(String name, String[] interfaces, @Nullable String superclass);
}
