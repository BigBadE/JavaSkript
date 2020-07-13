package software.bigbade.javaskript.compiler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import software.bigbade.javaskript.api.SkriptLineConverter;
import software.bigbade.javaskript.compiler.java.SkriptToJavaConverter;
import software.bigbade.javaskript.compiler.utils.MD5Checksum;
import software.bigbade.skriptasm.parser.ScriptParser;
import software.bigbade.skriptasm.parser.types.SkriptRegisteredTypes;

import java.io.File;

@RequiredArgsConstructor
public class JavaCompiler {
    @Getter
    private final File dataFolder;
    private final SkriptRegisteredTypes types;

    public void loadScripts() {
        File scriptsDir = new File(getDataFolder(), "scripts");
        File cache = new File(getDataFolder(), "cache");
        File[] scripts = scriptsDir.listFiles();
        if(scriptsDir.isDirectory() && scripts != null) {
            for (File script : scripts) {
                String name = script.getName();
                if(!name.endsWith(".sk") || name.startsWith("-")) {
                    continue;
                }
                name = name.substring(0, name.length()-2);
                String checksum = MD5Checksum.getMD5Checksum(script);
                File outputJar = new File(cache, name + File.pathSeparator + checksum + ".jar");
                if(!outputJar.exists()) {
                    SkriptLineConverter converter = new SkriptToJavaConverter(name);
                    ScriptParser parser = new ScriptParser(script, types, converter);
                    parser.parse();
                    converter.writeData(outputJar);
                }
                loadJar(outputJar);
            }
        }
    }

    public void loadJar(File jar) {

    }
}
