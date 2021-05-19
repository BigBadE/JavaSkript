package com.bigbade.javaskript.parser.register;

import com.bigbade.javaskript.api.skript.addon.ISkriptFunctionDef;
import com.bigbade.javaskript.api.skript.addon.ISkriptLiteralAddon;
import com.bigbade.javaskript.api.skript.addon.ISkriptSerializerAddon;
import com.bigbade.javaskript.api.skript.addon.ISkriptStringAddon;
import com.bigbade.javaskript.api.skript.addon.SkriptPattern;
import com.bigbade.javaskript.api.skript.code.ISkriptEffect;
import com.bigbade.javaskript.api.skript.code.ISkriptExpression;
import com.bigbade.javaskript.api.skript.code.ISkriptInstruction;
import com.bigbade.javaskript.parser.api.SkriptAddonEffect;
import com.bigbade.javaskript.parser.api.SkriptAddonExpression;
import com.bigbade.javaskript.parser.api.SkriptAddonInstruction;
import com.bigbade.javaskript.parser.api.SkriptFunctionDef;
import lombok.Getter;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public final class AddonManager {
    @Getter
    private final List<ISkriptFunctionDef> addonDefs = new ArrayList<>();
    @Getter
    private final List<ISkriptExpression> addonExpressions = new ArrayList<>();
    @Getter
    private final List<ISkriptEffect> addonEffects = new ArrayList<>();
    @Getter
    private final List<ISkriptInstruction> addonInstructions = new ArrayList<>();
    @Getter
    private final List<ISkriptSerializerAddon> addonSerializers = new ArrayList<>();
    @Getter
    private final List<ISkriptStringAddon<?>> stringAddons = new ArrayList<>();
    @Getter
    private final List<ISkriptLiteralAddon<?>> literalAddons = new ArrayList<>();

    private final Set<Class<? extends ISkriptFunctionDef>> overridingDefs = new HashSet<>();
    private final Set<Class<?>> overridingInstructions = new HashSet<>();
    private final Set<Class<? extends ISkriptSerializerAddon>> overridingSerializers = new HashSet<>();
    private final Set<Class<? extends ISkriptStringAddon<?>>> overridingStringAddons = new HashSet<>();
    private final Set<Class<? extends ISkriptLiteralAddon<?>>> overridingLiteralAddons = new HashSet<>();

    /**
     * Gets the methods annotated with SkriptPattern
     *
     * @param clazz Class that has the methods
     * @return All methods with the annotation
     * @see SkriptPattern
     */
    public static List<Method> getStatementMethods(Class<?> clazz) {
        List<Method> methods = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (Modifier.isPublic(method.getModifiers()) && method.isAnnotationPresent(SkriptPattern.class)) {
                methods.add(method);
            }
        }

        if (methods.isEmpty()) {
            throw new IllegalStateException(clazz.getSimpleName() + " has no public method annotated by @CompiledPattern");
        }

        return methods;
    }

    /**
     * Registers a literal addon, allowing addons to add literal interpreters
     * @param addon Literal addon
     * @param overriding Literal addons to override
     * @see ISkriptLiteralAddon
     */
    @SafeVarargs
    public final void registerLiteralAddon(ISkriptLiteralAddon<?> addon, Class<? extends ISkriptLiteralAddon<?>>... overriding) {
        if(overridingLiteralAddons.contains(addon.getClass())) {
            return;
        }
        literalAddons.add(addon);
        if(overriding.length == 0) return;
        for(Iterator<ISkriptLiteralAddon<?>> iterator = literalAddons.iterator(); iterator.hasNext();) {
            ISkriptLiteralAddon<?> testingAddon = iterator.next();
            for(Class<?> overridden : overriding) {
                if (testingAddon.getClass().equals(overridden)) {
                    iterator.remove();
                }
            }
        }
        overridingLiteralAddons.addAll(Arrays.asList(overriding));
    }

    /**
     * Registers a String addon, allowing addons to add custom features to strings (like sting interpolation)
     * @param addon String addon
     * @param overriding String addons to override
     * @see ISkriptStringAddon
     */
    @SafeVarargs
    public final void registerStringAddon(ISkriptStringAddon<?> addon, Class<? extends ISkriptStringAddon<?>>... overriding) {
        if(overridingStringAddons.contains(addon.getClass())) {
            return;
        }
        stringAddons.add(addon);
        if(overriding.length == 0) return;
        for(Iterator<ISkriptStringAddon<?>> iterator = stringAddons.iterator(); iterator.hasNext();) {
            ISkriptStringAddon<?> testingAddon = iterator.next();
            for(Class<?> overridden : overriding) {
                if (testingAddon.getClass().equals(overridden)) {
                    iterator.remove();
                }
            }
        }
        overridingStringAddons.addAll(Arrays.asList(overriding));
    }

    /**
     * Registers an addon definition.
     *
     * @param instruction Instruction to register
     * @param overriding Instructions to override
     * @see SkriptAddonExpression
     * @see SkriptAddonEffect
     */
    public void registerInstruction(Class<?> instruction, Class<?>... overriding) {
        if(overridingInstructions.contains(instruction)) {
            return;
        }
        for (Method target : getStatementMethods(instruction)) {
            SkriptAddonInstruction addonInstruction;
            if (target.getReturnType() != Void.TYPE) {
                addonInstruction = new SkriptAddonExpression(target,
                        target.getAnnotationsByType(SkriptPattern.class));
                addonExpressions.add((SkriptAddonExpression) addonInstruction);
            } else {
                addonInstruction = new SkriptAddonEffect(target,
                        target.getAnnotationsByType(SkriptPattern.class));
                addonEffects.add((SkriptAddonEffect) addonInstruction);
            }
            addonInstructions.add(addonInstruction);
        }

        if(overriding.length == 0) return;

        for(Iterator<ISkriptInstruction> iterator = addonInstructions.iterator(); iterator.hasNext();) {
            ISkriptInstruction foundInstruction = iterator.next();
            for(Class<?> override : overriding) {
                if(foundInstruction.getMethod().getDeclaringClass().equals(override)) {
                    iterator.remove();
                    if(foundInstruction instanceof ISkriptEffect) {
                        addonEffects.remove(foundInstruction);
                    } else if(foundInstruction instanceof ISkriptExpression) {
                        addonExpressions.remove(foundInstruction);
                    } else {
                        throw new IllegalStateException("Unknown ISkriptInstruction subclass " + foundInstruction.getClass());
                    }
                }
            }
        }

        overridingInstructions.addAll(Arrays.asList(overriding));
    }

    /**
     * Registers an addon definition.
     *
     * @param addonDef   Addon definition to register
     * @param overriding Function defs to override
     * @see SkriptFunctionDef
     */
    @SafeVarargs
    public final void registerMethodDef(ISkriptFunctionDef addonDef, Class<ISkriptFunctionDef>... overriding) {
        if(overridingDefs.contains(addonDef.getClass())) {
            return;
        }
        addonDefs.add(addonDef);

        if(overriding.length == 0) return;

        for(Iterator<ISkriptFunctionDef> iterator = addonDefs.iterator(); iterator.hasNext();) {
            ISkriptFunctionDef functionDef = iterator.next();
            for(Class<ISkriptFunctionDef> clazz : overriding) {
                if(functionDef.getClass().equals(clazz)) {
                    iterator.remove();
                }
            }
        }
        overridingDefs.addAll(Arrays.asList(overriding));
    }
}