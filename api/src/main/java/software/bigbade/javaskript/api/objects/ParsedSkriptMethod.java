package software.bigbade.javaskript.api.objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import software.bigbade.javaskript.api.variables.SkriptType;

import javax.annotation.Nullable;

@RequiredArgsConstructor
public class ParsedSkriptMethod {
    @Getter
    private final SkriptMethod method;
    @Nullable
    @Getter
    private final SkriptType<?> returnType;
}
