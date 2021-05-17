package com.bigbade.javaskript.api.java.defs;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IPackageDef {
    String getNamespace();

    List<IPackageDef> getSubpackages();

    Map<String, IJavaFile> getJavaFiles();

    Optional<IJavaFile> getJavaFile(String name);
}
