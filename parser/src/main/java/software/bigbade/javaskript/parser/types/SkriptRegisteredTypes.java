package software.bigbade.javaskript.parser.types;

import lombok.Getter;
import software.bigbade.javaskript.api.objects.SkriptMethod;
import software.bigbade.javaskript.api.objects.SkriptStructuredObject;

import java.util.HashSet;
import java.util.Set;

public final class SkriptRegisteredTypes {
    @Getter
    private static final Set<SkriptStructuredObject> structuredObjects = new HashSet<>();
    @Getter
    private static final Set<SkriptMethod> methods = new HashSet<>();

    private SkriptRegisteredTypes() {}

    public static void registerStructuredObject(SkriptStructuredObject object) {
        structuredObjects.add(object);
    }

    public static void registerMethod(SkriptMethod method) {
        methods.add(method);
    }
}
