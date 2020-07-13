package software.bigbade.javaskript.bukkit;

import org.bukkit.plugin.java.JavaPlugin;
import software.bigbade.skriptasm.parser.types.SkriptRegisteredTypes;

public class Skript extends JavaPlugin {
    private final SkriptRegisteredTypes types = new SkriptRegisteredTypes();

    @Override
    public void onEnable() {
        BukkitCompiler compiler = new BukkitCompiler(getDataFolder(), types);
        compiler.loadScripts();
    }
}
