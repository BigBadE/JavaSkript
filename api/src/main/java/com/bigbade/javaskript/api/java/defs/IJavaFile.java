package com.bigbade.javaskript.api.java.defs;

import java.util.List;

/**
 * Represents a java file. Each java file can have classes and imports.
 */
public interface IJavaFile {
    /**
     * Gets the class with the same name as the file
     * @return Main class
     */
    IClassDef getMainClass();

    /**
     * Gets all classes in the file. Includes private classes, doesn't include static classes in classes
     * (those are class members)
     * @return All classes in the file
     */
    List<IClassDef> getClasses();

    /**
     * Gets the package the file is in
     * @return Package of the file.
     */
    IPackageDef getPackageDef();

    /**
     * Gets the path to the file, relative to the root of the jar
     * @return Path to the file
     */
    String getFilePath();

    /**
     * Gets or creates a class with the given name
     * @param name Name of the class
     * @return Class with that name
     */
    IClassDef getClassForName(String name);

    /**
     * Checks if a class exists
     * @param name Name of the class
     * @return If the class exists
     */
    boolean classExists(String name);

    /**
     * Gets all the classes imported by this class.
     * @return Imports of the file
     */
    List<IImportDef> getImports();
}
