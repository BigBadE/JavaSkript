package software.bigbade.javaskript.compiler.factory;

import software.bigbade.javaskript.api.objects.SkriptLineConverter;
import software.bigbade.javaskript.api.factory.LineConverterFactory;
import software.bigbade.javaskript.compiler.java.BasicJavaClass;

import javax.annotation.Nullable;

public class JavaLineConverterFactory extends LineConverterFactory {
    @Override
    public SkriptLineConverter createLineConverter(String name, String[] interfaces, @Nullable String superclass) {
        return new BasicJavaClass(name, interfaces, superclass);
    }
}
