package software.bigbade.javaskript.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import software.bigbade.javaskript.api.SkriptLogger;
import software.bigbade.javaskript.parser.JavaCompiler;

import java.io.File;
import java.util.logging.Level;

public class BukkitCompiler extends JavaCompiler {
    public BukkitCompiler(File dataFolder) {
        super(dataFolder);
    }

    @Override
    public void loadJar(File jar) {
        try {
            Bukkit.getPluginManager().loadPlugin(jar);
        } catch (InvalidPluginException | InvalidDescriptionException e) {
            SkriptLogger.getLogger().log(Level.SEVERE, "Could not load jar", e);
        }
    }
}
