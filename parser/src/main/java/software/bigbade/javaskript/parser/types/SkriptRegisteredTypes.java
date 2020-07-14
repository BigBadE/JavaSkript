package software.bigbade.javaskript.parser.types;

import lombok.Getter;
import software.bigbade.javaskript.api.objects.SkriptMethod;
import software.bigbade.javaskript.api.objects.SkriptStructuredObject;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SkriptRegisteredTypes {
    private final List<SkriptStructuredObject> structuredObjects = new ArrayList<>();
    private final List<SkriptMethod> methods = new ArrayList<>();

    public void registerStructuredObject(SkriptStructuredObject object) {
        structuredObjects.add(object);
    }

    public void registerMethod(SkriptMethod method) {
        methods.add(method);
    }
}
