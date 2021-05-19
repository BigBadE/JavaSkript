package com.bigbade.javaskript.api.java.defs;

import java.util.List;
import java.util.Map;

public interface IPackageDef {
    String getNamespace();

    List<IPackageDef> getSubpackages();

    Map<String, IJavaFile> getJavaFiles();

    IJavaFile getJavaFile(String name);

    IPackageDef getSubpackage(String name);

    boolean fileExists(String name);

    boolean packageExists(String name);
}
