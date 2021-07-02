package com.bigbade.javaskript.api.java.defs;

import java.util.List;
import java.util.Map;

/**
 * A java package
 */
public interface IPackageDef {
    /**
     * Namespace of the package, such as com.bigbade.javaskript
     * @return Package namespacec
     */
    String getNamespace();

    /**
     * All subpackages contained in this package
     * @return All subpackages
     */
    List<IPackageDef> getSubpackages();

    /**
     * A map of the file name and java files in the package
     * @return Java files in the package
     */
    Map<String, IJavaFile> getJavaFiles();

    /**
     * Gets the path to the folder relative to the root of the jar
     * @return Path to the folder
     */
    String getFolderPath();

    /**
     * Returns the java file by name
     * @param name Name of the java file
     * @return Java file
     */
    IJavaFile getJavaFile(String name);

    /**
     * Gets a subpackage by name
     * @param name Subpackage name
     * @return Subpackage
     */
    IPackageDef getSubpackage(String name);

    /**
     * Checks if a file exists
     * @param name Name of the file
     * @return If the file exists
     */
    boolean fileExists(String name);

    /**
     * Checks if a subpackage exists
     * @param name Name of the subpackage
     * @return If the subpackage exists
     */
    boolean packageExists(String name);
}
