package com.bigbade.javaskript.api.skript.addon;

import com.bigbade.javaskript.api.java.defs.IPackageDef;
import com.bigbade.javaskript.api.skript.code.ITranslatorFactory;
import com.bigbade.javaskript.api.skript.defs.IValueTranslator;
import com.bigbade.javaskript.api.skript.pattern.ISkriptPattern;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * A definition inside the Skript, such as event listeners.
 * @param <T> Type of the starting translator (or void if none)
 */
@SuppressWarnings("unused")
public interface ISkriptFunctionDef<T> {
    /**
     * YAML-style key/value pairs, with code being an exception. Empty for none, null key for no key.
     * @return Yaml values of the def
     */
    @Nullable
    Map<String, IValueTranslator<?>> getTranslators();

    /**
     * Allows functions to have a single translator and no keys. If this has a non-null
     * value, the map returned by getYamlValues is completely ignored
     * @return Starting translator, or null if there are multiple translators
     */
    @Nullable
    IValueTranslator<?> getStartingTranslator();

    /**
     * Gets the patterns of the def
     * @return Def's patterns
     */
    List<ISkriptPattern> getPatterns();

    /**
     * Initializes the def with a TranslatorFactory, to give access to code parsing
     * @param patterns Patterns fetched from the SkriptPattern annotation
     * @param factory Translator factory
     * @see ITranslatorFactory
     * @see SkriptPattern
     */
    void init(List<ISkriptPattern> patterns, ITranslatorFactory factory);

    /**
     * Access to the rest of the project, for registering the def. For example, if you want the code to run
     * in the main method (or onEnable for Bukkit), get the class "Main" from the main package, get the
     * classes and find the one called "Main".
     * @param yamlValues Key/value pair specified
     * @param patternData Pattern data associated with the passed pattern
     * @param mainPackage Main package
     */
    void operate(Map<String, ?> yamlValues, int patternData, IPackageDef mainPackage);

    /**
     * The same as the above method, but with a single translator instead of a map.
     * @param startingValue Starting value
     * @param patternData Pattern data associated with the passed pattern
     * @param mainPackage Main package
     */
    void operate(T startingValue, int patternData, IPackageDef mainPackage);
}
