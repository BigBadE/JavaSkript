package software.bigbade.javaskript.api.objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ParsedSkriptMethod {
    @Getter
    private final String name;
    @Getter
    private final SkriptMethod method;
    @Getter
    private final List<LocalVariable> variables = new ArrayList<>();

    public void addLocalVariable(LocalVariable variable) {
        variables.add(variable);
    }
}
