package software.bigbade.javaskript.bukkit;

import org.bukkit.plugin.java.JavaPlugin;
import software.bigbade.javaskript.api.SkriptLogger;
import software.bigbade.javaskript.api.factory.LineConverterFactory;
import software.bigbade.javaskript.bukkit.manager.PluginYmlManager;
import software.bigbade.javaskript.bukkit.manager.RegisterManager;
import software.bigbade.javaskript.compiler.factory.JavaLineConverterFactory;

public class Skript extends JavaPlugin {
    private final PluginYmlManager ymlManager = new PluginYmlManager();

    @Override
    public void onEnable() {
        SkriptLogger.setLogger(getLogger());
        LineConverterFactory.setFactory(new JavaLineConverterFactory());
        RegisterManager.registerAll(ymlManager);

        BukkitCompiler compiler = new BukkitCompiler(getDataFolder());
        compiler.loadScripts();
    }
}
