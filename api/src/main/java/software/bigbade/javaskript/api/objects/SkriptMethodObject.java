package software.bigbade.javaskript.api.objects;

import lombok.Getter;
import software.bigbade.javaskript.api.IScriptParser;
import software.bigbade.javaskript.api.patterns.PatternParser;
import software.bigbade.javaskript.api.variables.SkriptType;
import software.bigbade.javaskript.api.variables.Variables;

import java.util.List;

public abstract class SkriptMethodObject implements SkriptStructuredObject {
    //Types passed to the method itself
    @Getter
    public final List<SkriptType<?>> inputVariables;
    //Parses
    private final PatternParser patternParser;

    @Getter
    private final SkriptType<?> returnType;

    @Getter
    private Variables variables;

    public SkriptMethodObject(String pattern, List<SkriptType<?>> inputVariables, SkriptType<?> returnType) {
        this.inputVariables = inputVariables;
        this.returnType = returnType;
        this.patternParser = new PatternParser(pattern);
    }

    public ParsedSkriptObject parse(IScriptParser parser, String line) {
        if(!patternParser.parse(parser, line)) {
            return null;
        }
        return getParsedObject();
    }

    abstract ParsedSkriptObject getParsedObject();
}
