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
     * Gets all the classes imported by this class.
     * @return Imports of the file
     */
    List<IImportDef> getImports();
}
