package com.bigbade.javaskript.translator;

import com.bigbade.javaskript.api.java.defs.IPackageDef;
import com.bigbade.javaskript.api.skript.defs.ISkriptDef;
import com.bigbade.javaskript.api.skript.defs.ISkriptFile;
import com.bigbade.javaskript.translator.impl.JavaPackage;

public class SkriptTranslator {
    public IPackageDef translate(ISkriptFile file) {
        IPackageDef basePackage = new JavaPackage("com.bigbade.generated." + file.getFileName());
        for(ISkriptDef def : file.getSkriptDefs()) {

        }
        return basePackage;
    }
}
