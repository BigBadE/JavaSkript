package software.bigbade.javaskript.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import software.bigbade.javaskript.compiler.JavaCompiler;
import software.bigbade.javaskript.parser.types.SkriptRegisteredTypes;

import java.io.File;

public class BukkitCompiler extends JavaCompiler {
    public BukkitCompiler(File dataFolder, SkriptRegisteredTypes types) {
        super(dataFolder, types);
    }

    @Override
    public void loadJar(File jar) {
        try {
            Bukkit.getPluginManager().loadPlugin(jar);
        } catch (InvalidPluginException | InvalidDescriptionException e) {
            //SkriptLogger.getLogger().log(Level.SEVERE, "Test");
            //SkriptLogger.getLogger().log(Level.SEVERE, "Could not load jar", e);
        }
    }
}
