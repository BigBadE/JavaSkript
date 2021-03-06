package com.bigbade.javaskript.translator.impl;

import com.bigbade.javaskript.api.java.defs.IClassDef;
import com.bigbade.javaskript.api.java.defs.IImportDef;
import com.bigbade.javaskript.api.java.defs.IJavaFile;
import com.bigbade.javaskript.api.java.defs.IPackageDef;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class JavaFile implements IJavaFile {
    private final IPackageDef packageDef;
    private final String name;

    private final List<IImportDef> imports = new ArrayList<>();
    private final List<IClassDef> classes = new ArrayList<>();

    @Override
    public IClassDef getMainClass() {
        return getClassForName(name);
    }

    @Override
    public String getFilePath() {
        return packageDef.getFolderPath() + File.separator + name + ".class";
    }

    @Override
    public IClassDef getClassForName(String name) {
        for(IClassDef classDef : classes) {
            if(classDef.getClassName().equals(name)) {
                return classDef;
            }
        }
        return new JavaClassDef(this, name);
    }

    @Override
    public boolean classExists(String name) {
        for(IClassDef classDef : classes) {
            if(classDef.getClassName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public void addImport(IImportDef importDef) {
        imports.add(importDef);
    }

    public void addClass(IClassDef classDef) {
        classes.add(classDef);
    }
}
