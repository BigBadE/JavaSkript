package com.bigbade.javaskript.api.skript.addon;

import com.bigbade.javaskript.api.java.defs.IMethodDef;
import com.bigbade.javaskript.api.java.defs.IPackageDef;
import com.bigbade.javaskript.api.java.variables.IVariableDef;
import com.bigbade.javaskript.api.skript.annotations.SkriptPattern;
import com.bigbade.javaskript.api.skript.code.ITranslatorFactory;
import com.bigbade.javaskript.api.skript.code.IVariableFactory;
import com.bigbade.javaskript.api.skript.defs.IValueTranslator;
import com.bigbade.javaskript.api.skript.pattern.ISkriptPattern;

import javax.annotation.Nullable;
import java.util.List;
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
     * Gets the variables of the def
     * @return Def's variables
     */
    List<IVariableDef> getVariables();

    /**
     * Sets up the variables of the def
     * @param factory Variable factory
     */
    void setupVariables(IVariableFactory factory);

    /**
     * Initializes the def with a TranslatorFactory, to give access to code parsing
     * @param patterns Patterns fetched from the SkriptPattern annotation
     * @param factory Translator factory
     * @see ITranslatorFactory
     * @see SkriptPattern
     */
    void init(List<ISkriptPattern> patterns, ITranslatorFactory factory);

    /**
     * The code shaded into the returned method. Follows the following rules:
     *
     * - yamlvalues will be replaced with the actual values at runtime
     * - patternData will be replaced with the actual pattern data
     * - ICodeDef#execute will be replaced with the instructions in the ICodeDef
     * - Referenced fields/methods/classes will be automatically shaded in. They must
     * be purely functional (no references to instance methods), anything that depends on
     * the state of the program will break (Basically, static utils/constants only).
     * The only reference to the state of the program should be the input passed to the method.
     *
     * @param yamlValues Key/value pair specified
     * @param patternData Pattern data associated with the passed pattern
     */
    void operate(Map<String, ?> yamlValues, int patternData);

    /**
     * Access to the rest of the project, for registering the def. For example, if you want the code to run
     * in the main method (or onEnable for Bukkit), get the class "Main" from the main package, get the
     * classes and return a method called "Main". This method will only be called if there is no starting translator.
     *
     * @param yamlValues Key/value pair specified
     * @param patternData Pattern data associated with the passed pattern
     * @param mainPackage Main package of the program
     * @return Method to shade into
     */
    IMethodDef locate(Map<String, ?> yamlValues, int patternData, IPackageDef mainPackage);

    /**
     * The same as the above method, but with a single translator instead of a map. This method will only be called
     * if there is a starting translator.
     * @param startingValue Starting value
     * @param patternData Pattern data associated with the passed pattern
     */
    void operate(Object startingValue, int patternData);

    /**
     * The same as the above method, but with a single translator instead of a map. This method will only be called
     * if there is a starting translator.
     *
     * @param startingValue Starting value specified
     * @param patternData Pattern data associated with the passed pattern
     * @param mainPackage Main package of the program
     * @return Method to shade into
     */
    IMethodDef locate(Object startingValue, int patternData, IPackageDef mainPackage);
}
