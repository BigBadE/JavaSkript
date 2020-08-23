package software.bigbade.javaskript.api.objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import software.bigbade.javaskript.api.objects.variable.LocalVariable;
import software.bigbade.javaskript.api.variables.SkriptType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ParsedSkriptMethod {
    @Getter
    private final SkriptMethod method;
    @Nullable
    @Getter
    private final SkriptType returnType;
    @Getter
    private final List<LocalVariable<?>> variables = new ArrayList<>();

    public void addLocalVariable(LocalVariable<?> variable) {
        variables.add(variable);
    }
}
