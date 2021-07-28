package com.bigbade.javaskript.java.util;

import com.bigbade.javaskript.api.skript.addon.ISkriptFunctionDef;
import com.bigbade.javaskript.api.skript.defs.IParsingDef;
import com.bigbade.javaskript.java.JavaSkript;
import com.bigbade.javaskript.java.loader.AddonClassLoader;
import com.bigbade.javaskript.parser.SkriptParser;
import com.bigbade.javaskript.parser.api.SkriptFile;
import org.junit.jupiter.api.Assertions;

import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;

public class SkriptRunner {
    private static SkriptParser parser;

    private final Map<Class<? extends ISkriptFunctionDef>, Map<String, Predicate<Object>>> defClasses = new HashMap<>();
    private Class<? extends ISkriptFunctionDef> currentDef;
    private Map<String, Predicate<Object>> currentArguments;

    public SkriptRunner startDefClass(Class<? extends ISkriptFunctionDef> clazz) {
        if(currentDef != null) {
            defClasses.put(currentDef, currentArguments);
        }
        currentDef = clazz;
        currentArguments = new HashMap<>();
        return this;
    }

    public SkriptRunner addArgument(String key, Predicate<Object> value) {
        currentArguments.put(key, value);
        return this;
    }

    public void run(String testName, String script) {
        if(parser == null) {
            parser = new SkriptParser();
            List<URL> urls = new ArrayList<>();
            AddonClassLoader addonClassLoader = new AddonClassLoader(urls, JavaSkript.class.getClassLoader(),
                    parser.getLineParser().getAddonManager());

            //Class loaders and the parser are both thread-safe, so the jars should be loaded async.
            ExecutorService executor = Executors.newCachedThreadPool();
            JavaSkript.loadSkriptClasses(addonClassLoader, executor);

            if (currentDef != null) {
                defClasses.put(currentDef, currentArguments);
            }
        }

        SkriptFile file = parser.parseSkript(new StringReader(script), testName);
        List<IParsingDef> defs = file.getParsedFunctions();
        Assertions.assertEquals(defClasses.size(), defs.size());
        int i = 0;
        for(Map.Entry<Class<? extends ISkriptFunctionDef>, Map<String, Predicate<Object>>> entry : defClasses.entrySet()) {
            IParsingDef def = defs.get(i++);
            Assertions.assertEquals(entry.getKey(), def.getFunctionDef().getClass());
            for(Map.Entry<String, Predicate<Object>> keyValue : entry.getValue().entrySet()) {
                if(keyValue.getKey() == null) {
                    //Handle single translator defs
                    Assertions.assertTrue(keyValue.getValue().test(def.getCurrentTranslator()));
                } else {
                    //Multi-translator defs
                    Assertions.assertTrue(keyValue.getValue().test(def.getKeyValues().get(keyValue.getKey())));
                }
            }
            def.getFunctionDef().getTranslators();
        }
    }
}
