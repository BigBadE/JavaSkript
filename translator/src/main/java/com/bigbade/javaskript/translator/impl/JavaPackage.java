package com.bigbade.javaskript.translator.impl;

import com.bigbade.javaskript.api.java.defs.IJavaFile;
import com.bigbade.javaskript.api.java.defs.IPackageDef;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class JavaPackage implements IPackageDef {
    @Getter
    private final String namespace;
    @Getter
    private final List<IPackageDef> subpackages = new ArrayList<>();
    @Getter
    private final Map<String, IJavaFile> javaFiles = new HashMap<>();

    public void addSubpackage(IPackageDef subpackage) {
        subpackages.add(subpackage);
    }

    public void addJavaFile(String name, IJavaFile javaFile) {
        javaFiles.put(name, javaFile);
    }

    @Override
    public IJavaFile getJavaFile(String name) {
        return javaFiles.computeIfAbsent(name, newName -> new JavaFile(name));
    }

    @Override
    public IPackageDef getSubpackage(String name) {
        for(IPackageDef packageDef : subpackages) {
            if(packageDef.getNamespace().equals(name)) {
                return packageDef;
            }
        }
        IPackageDef output = new JavaPackage(name);
        subpackages.add(output);
        return output;
    }

    @Override
    public boolean fileExists(String name) {
        return javaFiles.containsKey(name);
    }

    @Override
    public boolean packageExists(String name) {
        for(IPackageDef packageDef : subpackages) {
            if(packageDef.getNamespace().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
