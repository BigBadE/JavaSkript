package software.bigbade.javaskript.parser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import software.bigbade.javaskript.api.objects.SkriptLineConverter;
import software.bigbade.javaskript.api.SkriptLogger;
import software.bigbade.javaskript.parser.utils.MD5Checksum;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.JarOutputStream;
import java.util.logging.Level;

@RequiredArgsConstructor
public class JavaCompiler {
    @Getter
    private final File dataFolder;

    private final File cache = new File(getDataFolder(), "cache");

    public void loadScripts() {
        File scriptsDir = new File(getDataFolder(), "scripts");
        if((!scriptsDir.exists() && !scriptsDir.mkdirs()) || (!scriptsDir.exists() && !cache.mkdirs())) {
            throw new IllegalStateException("Could not create needed folder");
        }
        File[] scripts = scriptsDir.listFiles();
        if(scriptsDir.isDirectory() && scripts != null) {
            for (File script : scripts) {
                loadScript(script);
            }
        }
    }

    public void loadScript(File script) {
        String name = script.getName();
        if(!name.endsWith(".sk") || name.startsWith("-")) {
            return;
        }
        name = name.substring(0, name.length()-2);
        String checksum = MD5Checksum.getMD5Checksum(script);
        File outputJar = new File(cache, name + File.pathSeparator + checksum + ".jar");
        if(!outputJar.exists()) {
            ScriptParser parser = new ScriptParser(script);
            parser.parse();
            try(JarOutputStream outputStream = new JarOutputStream(new FileOutputStream(outputJar))) {
                for(SkriptLineConverter clazz : parser.getClasses()) {
                    clazz.writeData(outputStream);
                }
            } catch (IOException e) {
                SkriptLogger.getLogger().log(Level.SEVERE, "Error writing class", e);
            }
        }
        loadJar(outputJar);
    }

    public void loadJar(File jar) {
        //TODO
    }
}
