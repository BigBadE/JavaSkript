package software.bigbade.javaskript.bukkit;

import org.bukkit.plugin.java.JavaPlugin;
import software.bigbade.javaskript.api.SkriptLogger;
import software.bigbade.javaskript.parser.types.SkriptRegisteredTypes;

public class Skript extends JavaPlugin {
    private final SkriptRegisteredTypes types = new SkriptRegisteredTypes();

    @Override
    public void onEnable() {
        SkriptLogger.setLogger(getLogger());

        BukkitCompiler compiler = new BukkitCompiler(getDataFolder(), types);
        compiler.loadScripts(JavaPlugin.class);
    }
}
