package com.bigbade.javaskript.parser.register;

import com.bigbade.javaskript.api.skript.addon.ISkriptFunctionDef;
import com.bigbade.javaskript.api.skript.code.ISkriptEffect;
import com.bigbade.javaskript.api.skript.code.ISkriptExpression;
import com.bigbade.javaskript.api.skript.code.ISkriptInstruction;
import com.bigbade.javaskript.parser.api.SkriptAddonEffect;
import com.bigbade.javaskript.parser.api.SkriptAddonExpression;
import com.bigbade.javaskript.parser.api.SkriptFunctionDef;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public final class AddonManager {
    @Getter
    private static final List<ISkriptFunctionDef> addonDefs = new ArrayList<>();
    @Getter
    private static final List<ISkriptExpression> addonExpressions = new ArrayList<>();
    @Getter
    private static final List<ISkriptEffect> addonEffects = new ArrayList<>();
    @Getter
    private static final List<ISkriptInstruction> addonInstructions = new ArrayList<>();

    private AddonManager() {}

    /**
     * Registers an addon definition.
     * @param addonDef Addon definition to register
     * @see SkriptFunctionDef
     */
    public static void registerMethodDef(ISkriptFunctionDef addonDef) {
        addonDefs.add(addonDef);
    }

    /**
     * Registers an addon definition.
     * @param instruction Instruction to register
     * @see SkriptAddonExpression
     * @see SkriptAddonEffect
     */
    public static void registerInstruction(ISkriptInstruction instruction) {
        if(instruction instanceof SkriptAddonExpression) {
            addonExpressions.add((SkriptAddonExpression) instruction);
        } else if(instruction instanceof SkriptAddonEffect) {
            addonEffects.add((SkriptAddonEffect) instruction);
        } else {
            throw new IllegalStateException("Unknown instruction type " + instruction + "!");
        }
        addonInstructions.add(instruction);
    }
}
