package com.bigbade.javaskript.api.skript.addon;

import com.bigbade.javaskript.api.skript.annotations.FunctionPattern;
import com.bigbade.javaskript.api.skript.annotations.SkriptPattern;
import com.bigbade.javaskript.api.skript.code.IBranchFunction;
import com.bigbade.javaskript.api.skript.code.ISkriptEffect;
import com.bigbade.javaskript.api.skript.code.ISkriptExpression;
import com.bigbade.javaskript.api.skript.code.ISkriptInstruction;
import com.bigbade.javaskript.api.skript.code.IVariableFactory;
import com.bigbade.javaskript.api.skript.defs.IBranchFunctionDef;

import java.util.List;

@SuppressWarnings({"unused", "unchecked"})
public interface IAddonManager {
    List<ISkriptFunctionDef> getAddonDefs();

    List<ISkriptExpression> getAddonExpressions();

    List<ISkriptEffect> getAddonEffects();

    List<ISkriptInstruction> getAddonInstructions();

    List<ISkriptSerializerAddon<?>> getAddonSerializers();

    List<ISkriptStringAddon<?>> getStringAddons();

    List<ISkriptLiteralAddon<?>> getLiteralAddons();

    List<IBranchFunction> getBranchFunctionDefs();

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
     * Registers a branch function, allowing changes to the control flow of a method.
     * @param branchFunction Branch function
     * @see ISkriptFunctionDef
     */
    void registerBranchFunction(IBranchFunctionDef branchFunction);

    /**
     * Registers an addon instruction. Addon instructions REQUIRE a SkriptPattern.
     *
     * @param instruction Instruction to register
     * @param overriding Instructions to override
     * @see SkriptPattern
     */
    void registerInstruction(Class<?> instruction, Class<?>... overriding);

    /**
     * Registers a function definition. Function definitions REQUIRE a FunctionPattern
     *
     * @param addonDef   Function definition to register
     * @param overriding Function defs to override
     * @see ISkriptFunctionDef
     * @see FunctionPattern
     */
    void registerFunctionDef(ISkriptFunctionDef addonDef, Class<ISkriptFunctionDef>... overriding);

    /**
     * Sets up method defs, registering their variables.
     *
     * @param variableFactory Variable factory
     */
    void setupDefs(IVariableFactory variableFactory);
}
