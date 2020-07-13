package software.bigbade.javaskript.api.objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SkriptObject {
    private final SkriptObject parent;
    private final SkriptStructuredObject type;
}
