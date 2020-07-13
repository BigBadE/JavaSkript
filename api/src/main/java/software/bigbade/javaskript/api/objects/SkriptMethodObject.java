package software.bigbade.javaskript.api.objects;

import software.bigbade.javaskript.api.patterns.PatternParser;
import software.bigbade.javaskript.api.variables.Variables;
import software.bigbade.javaskript.api.SkriptLineConverter;
import software.bigbade.javaskript.api.variables.SkriptType;

import java.util.List;

public class SkriptMethodObject implements SkriptStructuredObject {
    //Types passed to the method itself
    public final List<SkriptType<?>> inputVariables;
    //Parses
    private final PatternParser patternParser;

    private final SkriptType<?> returnType;

    private Variables variables;

    public SkriptMethodObject(String pattern, List<SkriptType<?>> structureVariables, List<SkriptType<?>> inputVariables, SkriptType<?> returnType) {
        this.inputVariables = inputVariables;
        this.returnType = returnType;
        this.patternParser = new PatternParser(pattern, structureVariables);
    }

    public boolean parse(String line) {
        variables = patternParser.parse(line);
        return variables != null;
    }

    @Override
    public void register(SkriptLineConverter lineConverter, String line) {
        line = line.trim();
        lineConverter.startMethod(line.substring(0, line.length()-1), variables, returnType);
    }
}
