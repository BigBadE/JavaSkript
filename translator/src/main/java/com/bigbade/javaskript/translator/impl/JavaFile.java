package com.bigbade.javaskript.translator.impl;

import com.bigbade.javaskript.api.java.defs.IClassDef;
import com.bigbade.javaskript.api.java.defs.IImportDef;
import com.bigbade.javaskript.api.java.defs.IJavaFile;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class JavaFile implements IJavaFile {
    private final String name;
    private final List<IImportDef> imports = new ArrayList<>();
    private final List<IClassDef> classes = new ArrayList<>();

    public void addImport(IImportDef importDef) {
        imports.add(importDef);
    }

    public void addClass(IClassDef classDef) {
        classes.add(classDef);
    }

    @Override
    public IClassDef getMainClass() {
        return getClassForName(name);
    }

    @Override
    public IClassDef getClassForName(String name) {
        for(IClassDef classDef : classes) {
            if(classDef.getClassName().equals(name)) {
                return classDef;
            }
        }
        return new JavaClassDef(name);
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
}
