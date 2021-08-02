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
     * Gets the value of the variable with the given name. Will only work in the operation method
     * The identifier MUST BE DECLARED IN THE ARGS due to how the translator figures out which variable you want.
     *
     * Example:
     * getVariable("event");
     * Incorrect:
     * String variable = "event";
     * getVariable(key);
     * @param identifier Name of the variable
     * @throws IllegalStateException If this method is called, when it should just be used as a placeholder
     */
    <T> T getVariable(String identifier);

    /**
     * Executes the code translator def with the given key. Single translators can use a null key.
     * The key MUST BE DECLARED IN THE ARGS due to how the translator figures out which translator you want to execute.
     *
     * Example:
     * execute("mykey");
     * Incorrect:
     * String key = "mykey";
     * execute(key);
     * @param key Key of the translator to execute
     * @throws IllegalStateException If this method is called, when it should just be used as a placeholder
     */
    void execute(@Nullable String key);

    /**
     * Initializes the def with a TranslatorFactory, to give access to code parsing
     * @param patterns Patterns fetched from the SkriptPattern annotation
     * @param factory Translator factory
     * @see ITranslatorFactory
     * @see SkriptPattern
     */
    void init(List<ISkriptPattern> patterns, ITranslatorFactory factory);

    /**
     * The code shaded into the returned method. Calling the execute method on a code block is allowed here and
     * the call will be replaced with the code in the block.
     *
     * The variables in the variable list
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
