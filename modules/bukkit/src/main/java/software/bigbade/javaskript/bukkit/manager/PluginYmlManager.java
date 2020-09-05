package software.bigbade.javaskript.bukkit.manager;

import software.bigbade.javaskript.api.SkriptLogger;
import software.bigbade.javaskript.bukkit.data.BukkitCommandData;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.logging.Level;

public class PluginYmlManager {
    private final Map<String, BukkitCommandData> commandDataMap = new HashMap<>();

    public void registerCommand(String name, BukkitCommandData commandData) {
        commandDataMap.put(name, commandData);
    }

    public void writePluginYml(JarOutputStream outputStream) {
        JarEntry jarEntry = new JarEntry("plugin.yml");
        try (OutputStreamWriter fileOutputStream = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)){
            outputStream.putNextEntry(jarEntry);
            fileOutputStream.write("commands:");
            for(Map.Entry<String, BukkitCommandData> entry : commandDataMap.entrySet()) {
                fileOutputStream.write("    " + entry.getKey() + ":");
                for(Map.Entry<String, String> commandData : entry.getValue().getCommandData().entrySet()) {
                    fileOutputStream.write("        " + commandData.getKey() + ": " + commandData.getValue());
                }
            }
            outputStream.closeEntry();
        } catch (IOException e) {
            SkriptLogger.getLogger().log(Level.SEVERE, "Error writing plugin.yml", e);
        }
    }
}
