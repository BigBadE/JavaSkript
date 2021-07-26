package com.bigbade.javaskript.parser.parsing;

import com.bigbade.javaskript.api.skript.annotations.FunctionPattern;
import com.bigbade.javaskript.api.skript.addon.IAddonManager;
import com.bigbade.javaskript.api.skript.addon.ISkriptFunctionDef;
import com.bigbade.javaskript.api.skript.addon.ISkriptLiteralAddon;
import com.bigbade.javaskript.api.skript.addon.ISkriptSerializerAddon;
import com.bigbade.javaskript.api.skript.addon.ISkriptStringAddon;
import com.bigbade.javaskript.api.skript.annotations.SkriptPattern;
import com.bigbade.javaskript.api.skript.code.IBranchFunction;
import com.bigbade.javaskript.api.skript.code.ISkriptEffect;
import com.bigbade.javaskript.api.skript.code.ISkriptExpression;
import com.bigbade.javaskript.api.skript.code.ISkriptInstruction;
import com.bigbade.javaskript.api.skript.code.ITranslatorFactory;
import com.bigbade.javaskript.api.skript.code.IVariableFactory;
import com.bigbade.javaskript.api.skript.defs.IBranchFunctionDef;
import com.bigbade.javaskript.api.skript.pattern.ISkriptPattern;
import com.bigbade.javaskript.parser.api.SkriptAddonEffect;
import com.bigbade.javaskript.parser.api.SkriptAddonExpression;
import com.bigbade.javaskript.parser.api.SkriptAddonInstruction;
import com.bigbade.javaskript.parser.impl.SkriptBranchInstruction;
import com.bigbade.javaskript.parser.pattern.CompiledPattern;
import lombok.Getter;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Handles registering all the classes needed for parsing, such as instructions and functions.
 */
@SuppressWarnings("unused")
public final class AddonManager implements IAddonManager {
    //All lists here are syncronized to prevent any problems with async registration.
    @Getter
    private final List<ISkriptFunctionDef> addonDefs = Collections.synchronizedList(new ArrayList<>());
    @Getter
    private final List<ISkriptExpression> addonExpressions = Collections.synchronizedList(new ArrayList<>());
    @Getter
    private final List<ISkriptEffect> addonEffects = Collections.synchronizedList(new ArrayList<>());
    @Getter
    private final List<ISkriptInstruction> addonInstructions = Collections.synchronizedList(new ArrayList<>());
    @Getter
    private final List<ISkriptSerializerAddon<?>> addonSerializers = Collections.synchronizedList(new ArrayList<>());
    @Getter
    private final List<ISkriptStringAddon<?>> stringAddons = Collections.synchronizedList(new ArrayList<>());
    @Getter
    private final List<ISkriptLiteralAddon<?>> literalAddons = Collections.synchronizedList(new ArrayList<>());
    @Getter
    private final List<IBranchFunction> branchFunctionDefs = Collections.synchronizedList(new ArrayList<>());

    private final Set<Class<? extends ISkriptFunctionDef>> overridingDefs = Collections.synchronizedSet(new HashSet<>());
    private final Set<Class<?>> overridingInstructions = Collections.synchronizedSet(new HashSet<>());
    private final Set<Class<? extends ISkriptSerializerAddon<?>>> overridingSerializers = Collections.synchronizedSet(new HashSet<>());
    private final Set<Class<? extends ISkriptStringAddon<?>>> overridingStringAddons = Collections.synchronizedSet(new HashSet<>());
    private final Set<Class<? extends ISkriptLiteralAddon<?>>> overridingLiteralAddons = Collections.synchronizedSet(new HashSet<>());

    private final ITranslatorFactory translatorFactory = new TranslatorFactory();

    private boolean setup;

    protected AddonManager() { }

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

        return methods;
    }

    /**
     * Registers a literal addon, allowing addons to add literal interpreters
     *
     * @param addon      Literal addon
     * @param overriding Literal addons to override
     * @see ISkriptLiteralAddon
     */
    @SafeVarargs
    public final void registerLiteralAddon(ISkriptLiteralAddon<?> addon,
                                           Class<? extends ISkriptLiteralAddon<?>>... overriding) {
        if (overridingLiteralAddons.contains(addon.getClass())) {
            return;
        }
        literalAddons.add(addon);
        if (overriding.length == 0) return;
        for (Iterator<ISkriptLiteralAddon<?>> iterator = literalAddons.iterator(); iterator.hasNext(); ) {
            ISkriptLiteralAddon<?> testingAddon = iterator.next();
            for (Class<?> overridden : overriding) {
                if (testingAddon.getClass().equals(overridden)) {
                    iterator.remove();
                }
            }
        }
        overridingLiteralAddons.addAll(Arrays.asList(overriding));
    }

    /**
     * Registers a String addon, allowing addons to add custom features to strings (like sting interpolation)
     *
     * @param addon      String addon
     * @param overriding String addons to override
     * @see ISkriptStringAddon
     */
    @SafeVarargs
    public final void registerStringAddon(ISkriptStringAddon<?> addon, Class<? extends ISkriptStringAddon<?>>... overriding) {
        if (overridingStringAddons.contains(addon.getClass())) {
            return;
        }
        stringAddons.add(addon);
        if (overriding.length == 0) return;
        for (Iterator<ISkriptStringAddon<?>> iterator = stringAddons.iterator(); iterator.hasNext(); ) {
            ISkriptStringAddon<?> testingAddon = iterator.next();
            for (Class<?> overridden : overriding) {
                if (testingAddon.getClass().equals(overridden)) {
                    iterator.remove();
                }
            }
        }
        overridingStringAddons.addAll(Arrays.asList(overriding));
    }

    /**
     * Registers a branch function, allowing changes to the control flow of a method.
     * Branch functions REQUIRE a SkriptPattern and must return the amount of times the code is executed.
     *
     * @param branchFunction Branch function
     * @see ISkriptFunctionDef
     */
    public void registerBranchFunction(IBranchFunctionDef branchFunction) {
        for (Method target : getStatementMethods(branchFunction.getClass())) {
            branchFunctionDefs.add(new SkriptBranchInstruction(branchFunction,
                    target, target.getAnnotationsByType(SkriptPattern.class)));
        }
    }


    /**
     * Registers an addon instruction. Addon instructions REQUIRE a SkriptPattern.
     *
     * @param instruction Instruction to register
     * @see SkriptPattern
     */
    public final void registerInstruction(Class<?> instruction) {
       registerInstruction(instruction, Collections.emptyList());
    }

    /**
     * Registers an addon instruction. Addon instructions REQUIRE a SkriptPattern.
     *
     * @param instruction Instruction to register
     * @param overriding  Instructions to override
     * @see SkriptPattern
     */
    public final void registerInstruction(Class<?> instruction,
                                          List<Class<?>> overriding) {
        if (overridingInstructions.contains(instruction)) {
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

        if (overriding.size() == 0) return;
        overrideDefs(overriding);
    }

    /**
     * Removes any overridden defs and adds it to the override list
     *
     * @param overriding Defs to override
     */
    private void overrideDefs(List<Class<?>> overriding) {
        Iterator<ISkriptInstruction> iterator = addonInstructions.iterator();
        while (iterator.hasNext()) {
            ISkriptInstruction foundInstruction = iterator.next();
            for (Class<?> override : overriding) {
                if (foundInstruction.getMethod().getDeclaringClass().equals(override)) {
                    iterator.remove();
                    if (foundInstruction instanceof ISkriptEffect) {
                        addonEffects.remove(foundInstruction);
                    } else if (foundInstruction instanceof ISkriptExpression) {
                        addonExpressions.remove(foundInstruction);
                    } else {
                        throw new IllegalStateException("Unknown ISkriptInstruction subclass " + foundInstruction.getClass());
                    }
                }
            }
        }

        overridingInstructions.addAll(overriding);
    }

    /**
     * Registers an addon definition. Addon definitions REQUIRE a SkriptPattern
     *
     * @param addonDef   Addon definition to register
     * @see ISkriptFunctionDef
     * @see SkriptPattern
     */
    public final void registerFunctionDef(ISkriptFunctionDef addonDef) {
        registerFunctionDef(addonDef, Collections.emptyList());
    }

    /**
     * Registers an addon definition. Addon definitions REQUIRE a SkriptPattern
     *
     * @param addonDef   Addon definition to register
     * @param overriding Function defs to override
     * @see ISkriptFunctionDef
     * @see SkriptPattern
     */
    public final void registerFunctionDef(ISkriptFunctionDef addonDef, List<Class<ISkriptFunctionDef>> overriding) {
        if (setup) {
            throw new IllegalStateException("Tried to register a defs after setup is complete!");
        }

        if (overridingDefs.contains(addonDef.getClass())) {
            return;
        }
        FunctionPattern[] skriptPatterns = addonDef.getClass().getAnnotationsByType(FunctionPattern.class);

        List<ISkriptPattern> patterns = new ArrayList<>();
        for (FunctionPattern pattern : skriptPatterns) {
            patterns.add(new CompiledPattern(pattern.pattern(), pattern.patternData()));
        }
        addonDef.init(patterns, translatorFactory);
        addonDefs.add(addonDef);

        if (overriding.size() == 0) return;

        Iterator<ISkriptFunctionDef> iterator = addonDefs.iterator();
        while (iterator.hasNext()) {
            ISkriptFunctionDef functionDef = iterator.next();
            for (Class<ISkriptFunctionDef> clazz : overriding) {
                if (functionDef.getClass().equals(clazz)) {
                    iterator.remove();
                }
            }
        }
        overridingDefs.addAll(overriding);
    }

    public void setupDefs(IVariableFactory variableFactory) {
        if (setup) {
            throw new IllegalStateException("Tried to setup already-setup defs!");
        }

        for (ISkriptFunctionDef def : addonDefs) {
            def.setupVariables(variableFactory);
        }
        setup = false;
    }
}
