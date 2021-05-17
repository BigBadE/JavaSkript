package com.bigbade.javaskript.api.skript.defs;

import java.util.List;

/**
 * Represents an entire Skript file in memory
 */
public interface ISkriptFile {
    /**
     * Gets the file name of the file being parsed.
     * @return Name of the file being parsed, with .sk removed
     */
    String getFileName();

    /**
     * Returns all the Skript defs in the Skript file.
     * @return Skript defs in this file
     */
    List<ISkriptDef> getSkriptDefs();

    /**
     * Adds a Skript def to this file.
     * @param def Skript def to add
     */
    void addSkriptDef(ISkriptDef def);
}
