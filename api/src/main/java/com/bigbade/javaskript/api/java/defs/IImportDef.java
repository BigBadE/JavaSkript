package com.bigbade.javaskript.api.java.defs;

import com.bigbade.javaskript.api.java.util.IClassType;
import com.bigbade.javaskript.api.java.util.Modifiers;

/**
 * Defines an import, allowing classes to access other classes with simple names.
 */
@SuppressWarnings("unused")
public interface IImportDef {
    /**
     * Gets the imported class by this definition.
     * @return Imported class
     */
    IClassType getImporting();

    /**
     * Gets the modifiers of this import statement. Only allows the static modifier
     * @return Modifiers of this definition
     */
    Modifiers getModifiers();
}
