package software.bigbade.javaskript.parser.api;

import lombok.Getter;
import lombok.Setter;
import software.bigbade.javaskript.api.variables.Variables;
import software.bigbade.javaskript.parser.LoadingPhase;

public class SkriptFile {
    @Getter
    private Variables variables;

    @Getter
    @Setter
    private LoadingPhase phase;
}