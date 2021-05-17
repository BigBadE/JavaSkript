package com.bigbade.javaskript.parser;

import com.bigbade.javaskript.api.skript.defs.ISkriptDef;
import com.bigbade.javaskript.parser.api.SkriptFile;
import org.junit.jupiter.api.Assertions;

import javax.annotation.Nonnull;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

public class SkriptRunner {
    public static void run(String testName, String script, @Nonnull Map<Class<ISkriptDef>, Map<String, ?>> defClasses) {
        SkriptFile file = new SkriptParser(testName).parseSkript(new StringReader(script));
        List<ISkriptDef> defs = file.getSkriptDefs();
        Assertions.assertEquals(defClasses.size(), defs.size());
        int i = 0;
        for(Map.Entry<Class<ISkriptDef>, Map<String, ?>> entry : defClasses.entrySet()) {
            ISkriptDef def = defs.get(i++);
            Assertions.assertEquals(entry.getKey(), def.getClass());
            for(Map.Entry<String, ?> keyValue : entry.getValue().entrySet()) {
                Assertions.assertEquals(def.getKeyValues().get(keyValue.getKey()), keyValue.getValue());
            }
        }
    }
}
