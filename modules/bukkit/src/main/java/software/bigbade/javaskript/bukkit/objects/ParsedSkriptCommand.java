package software.bigbade.javaskript.bukkit.objects;

import software.bigbade.javaskript.api.exception.IllegalScriptException;
import software.bigbade.javaskript.api.instructions.Statements;
import software.bigbade.javaskript.api.objects.ParsedSkriptObject;
import software.bigbade.javaskript.api.objects.SkriptLineConverter;
import software.bigbade.javaskript.api.patterns.PatternParser;
import software.bigbade.javaskript.api.variables.ArrayType;
import software.bigbade.javaskript.api.variables.SkriptType;
import software.bigbade.javaskript.api.variables.SkriptTypes;
import software.bigbade.javaskript.api.variables.Type;
import software.bigbade.javaskript.api.variables.Variables;
import software.bigbade.javaskript.bukkit.data.BukkitCommandData;
import software.bigbade.javaskript.bukkit.manager.PluginYmlManager;
import software.bigbade.javaskript.bukkit.types.BukkitTypes;
import software.bigbade.javaskript.compiler.statements.IfStatementType;
import software.bigbade.javaskript.compiler.variables.StoredVariable;

import java.util.HashMap;
import java.util.Map;

public class ParsedSkriptCommand extends ParsedSkriptObject {
    private final Map<String, String> commandData = new HashMap<>();
    private final PluginYmlManager ymlManager;
    private final Variables methodVariables;
    private final String name;

    private boolean parsingMethod = false;

    private static final Variables commandVariables = new Variables();

    static {
        commandVariables.addVariable("sender", BukkitTypes.COMMAND_SENDER);
        commandVariables.addVariable("command", BukkitTypes.COMMAND);
        commandVariables.addVariable("label", SkriptTypes.STRING);
        commandVariables.addVariable("args", new ArrayType<>(SkriptTypes.STRING));
    }

    public ParsedSkriptCommand(PluginYmlManager ymlManager, SkriptLineConverter converter, Variables methodVariables, String name) {
        super(converter.startMethod(name + "Command", commandVariables, null));
        this.methodVariables = methodVariables;
        this.ymlManager = ymlManager;
        this.name = name;
    }

    @Override
    public boolean shouldParseMethods() {
        return parsingMethod;
    }

    @Override
    public void parseLine(String line) {
        String[] data = PatternParser.SPLIT_PATTERN.split(line, 2);
        switch (data[0].toLowerCase()) {
            case "trigger:":
                parsingMethod = true;
                int i = 0;
                for(Map.Entry<String, SkriptType<?>> entry : methodVariables.getAllVariables().entrySet()) {
                    getConverter().addJavaBlock(Statements.CODE_BLOCK)
                            .loadArrayVariable(getConverter().getLocalVariable("args"), i)
                            .setVariable(new StoredVariable<>(entry.getValue().getType()));
                    i++;
                }
                break;
            case "description:":
                if(data.length == 1) {
                    throw new IllegalScriptException("Command description is incorrect!");
                }
                commandData.put("description", data[1]);
                break;
            case "executable":
                if(data.length != 2 || !data[1].startsWith("by: ")) {
                    throw new IllegalScriptException("Illegal executable by!");
                }
                String output = data[1].toLowerCase().substring(4);
                Type executor;
                switch (output) {
                    case "players":
                    case "player":
                        executor = BukkitTypes.PLAYER.getType();
                        break;
                    default:
                        throw new IllegalScriptException("Unknown executor: " + output);
                }
                getConverter().loadVariable(getConverter().getLocalVariable("sender"));
                getConverter().isInstanceOf(executor);
                getConverter().addJavaBlock(Statements.IF_STATEMENTS, IfStatementType.EQUALS);
                getConverter().addJavaBlock(Statements.CODE_BLOCK);
                //TODO send other error message? Check how Skript does it.
                getConverter().loadConstant(false);
                getConverter().returnVariable();
                getConverter().endJavaBlock();
                getConverter().endJavaBlock();
                break;
            case "permission:":
                if(data.length == 1) {
                    throw new IllegalScriptException("Command permission is empty!");
                } else if(data.length > 2) {
                    throw new IllegalScriptException("Command permission is over one word!");
                }
                commandData.put("permission", data[1]);
                break;
            default:
                throw new IllegalScriptException("Unknown command argument: " + data[0].toLowerCase());
        }
    }

    @Override
    public ParsedSkriptObject getParent() {
        if(parsingMethod) {
            parsingMethod = false;
            return this;
        } else {
            ymlManager.registerCommand(name, new BukkitCommandData(commandData));
            return super.getParent();
        }
    }

    @Override
    public void finishParsing() {
        parsingMethod = false;
    }
}
