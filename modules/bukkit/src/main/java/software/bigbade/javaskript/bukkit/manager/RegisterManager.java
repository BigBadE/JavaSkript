package software.bigbade.javaskript.bukkit.manager;

import software.bigbade.javaskript.bukkit.objects.SkriptCommand;
import software.bigbade.javaskript.parser.types.SkriptRegisteredTypes;

public final class RegisterManager {
    private RegisterManager() { }

    public static void registerAll(PluginYmlManager ymlManager) {
        SkriptRegisteredTypes.registerStructuredObject(new SkriptCommand(ymlManager));
    }
}
