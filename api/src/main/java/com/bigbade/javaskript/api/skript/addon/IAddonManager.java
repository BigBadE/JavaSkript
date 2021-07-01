package com.bigbade.javaskript.api.skript.addon;

import com.bigbade.javaskript.api.skript.code.IVariableFactory;

@SuppressWarnings({"unused", "unchecked"})
public interface IAddonManager {
    /**
     * Registers a literal addon, allowing addons to add literal interpreters
     * @param addon Literal addon
     * @param overriding Literal addons to override
     * @see ISkriptLiteralAddon
     */
    void registerLiteralAddon(ISkriptLiteralAddon<?> addon, Class<? extends ISkriptLiteralAddon<?>>... overriding);

    /**
     * Registers a String addon, allowing addons to add custom features to strings (like sting interpolation)
     * @param addon String addon
     * @param overriding String addons to override
     * @see ISkriptStringAddon
     */
    void registerStringAddon(ISkriptStringAddon<?> addon, Class<? extends ISkriptStringAddon<?>>... overriding);

    /**
     * Registers an addon instruction. Addon instructions REQUIRE a SkriptPattern.
     *
     * @param instruction Instruction to register
     * @param overriding Instructions to override
     * @see SkriptPattern
     */
    void registerInstruction(Class<?> instruction, Class<?>... overriding);

    /**
     * Registers an addon definition. Addon definitions REQUIRE a SkriptPattern
     *
     * @param addonDef   Addon definition to register
     * @param overriding Function defs to override
     * @see ISkriptFunctionDef
     * @see SkriptPattern
     */
    void registerMethodDef(ISkriptFunctionDef addonDef, Class<ISkriptFunctionDef>... overriding);

    /**
     * Sets up method defs, registering their variables.
     * @param variableFactory Variable factory
     */
    void setupDefs(IVariableFactory variableFactory);
}
