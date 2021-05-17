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
    public Optional<IJavaFile> getJavaFile(String name) {
        return Optional.ofNullable(javaFiles.get(name));
    }
}
