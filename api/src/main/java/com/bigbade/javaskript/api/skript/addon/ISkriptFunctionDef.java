package com.bigbade.javaskript.api.skript.addon;

import com.bigbade.javaskript.api.java.defs.IPackageDef;
import com.bigbade.javaskript.api.skript.defs.IValueTranslator;
import com.bigbade.javaskript.api.skript.pattern.ISkriptPattern;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * A definition inside the Skript, such as event listeners.
 */
@SuppressWarnings("unused")
public interface ISkriptFunctionDef {
    /**
     * YAML-style key/value pairs, with code being an exception. Empty for none, null key for no key.
     * @return Yaml values of the def
     */
    @Nullable
    Map<String, IValueTranslator<Object>> getYamlValues();

    /**
     * Allows functions to have a single translator and no keys. If this has a non-null
     * value, the map returned by getYamlValues is completely ignored
     * @return Starting translator, or null if there are multiple translators
     */
    @Nullable
    IValueTranslator<Object> getStartingTranslator();

    /**
     * The patterns of the definition.
     * @return Definition patterns and pattern data
     */
    Map<ISkriptPattern, Object> getPatterns();

    /**
     * What class this def should be put into, packages should be prepended and separated with a ".".
     * @return Name of target class, can be in packages
     */
    @Nullable
    String getTargetClass();

    /**
     * Access to the rest of the project, for registering the def. For example, if you want the code to run
     * in the main method (or onEnable for Bukkit), get the class "Main" from the main package, get the
     * classes and find the one called "Main".
     * @param yamlValues Key/value pair specified
     * @param patternData Pattern data associated with the passed pattern
     * @param mainPackage Main package
     */
    void translate(Map<String, ?> yamlValues, Object patternData, IPackageDef mainPackage);
}
