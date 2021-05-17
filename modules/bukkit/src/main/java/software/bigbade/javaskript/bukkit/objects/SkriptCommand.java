package software.bigbade.javaskript.bukkit.objects;

import lombok.RequiredArgsConstructor;
import software.bigbade.javaskript.api.IScriptParser;
import software.bigbade.javaskript.api.exception.IllegalScriptException;
import software.bigbade.javaskript.api.objects.ParsedSkriptObject;
import software.bigbade.javaskript.api.objects.SkriptLineConverter;
import software.bigbade.javaskript.api.objects.SkriptStructuredObject;
import software.bigbade.javaskript.api.patterns.PatternParser;
import software.bigbade.javaskript.api.variables.SkriptType;
import software.bigbade.javaskript.api.variables.SkriptTypes;
import software.bigbade.javaskript.api.variables.Variables;
import software.bigbade.javaskript.bukkit.manager.PluginYmlManager;

@RequiredArgsConstructor
public class SkriptCommand implements SkriptStructuredObject {
    private final PluginYmlManager ymlManager;

    @Override
    public ParsedSkriptObject parse(IScriptParser parser, SkriptLineConverter converter, String line) {
        Variables variables = new Variables();
        String[] lines = PatternParser.SPLIT_PATTERN.split(line.substring(0, line.length()-1));
        if(lines.length < 2 || !lines[0].equalsIgnoreCase("command")) {
            return null;
        }
        for(int i = 1; i < lines.length-1; i++) {
            String found = lines[i+1].substring(1, lines[i+1].length()-1).toLowerCase();
            SkriptType<?> type = SkriptTypes.getSkriptType(found).orElseThrow(() -> new IllegalScriptException("Unknown type " + found));
            if(!type.isSet()) {
                throw new IllegalScriptException("Type " + found + " cannot be a command argument");
            }
            variables.addVariable("args-" + i, type);
        }
        return new ParsedSkriptCommand(ymlManager, converter, variables, lines[1]);
    }
}
