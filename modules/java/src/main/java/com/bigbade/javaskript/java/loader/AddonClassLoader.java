package com.bigbade.javaskript.java.loader;

import com.bigbade.javaskript.api.skript.addon.IAddonManager;
import com.bigbade.javaskript.api.skript.addon.ISkriptFunctionDef;
import com.bigbade.javaskript.api.skript.defs.IBranchFunctionDef;
import com.bigbade.javaskript.java.JavaSkript;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class AddonClassLoader extends URLClassLoader {
    private final IAddonManager addonManager;

    public AddonClassLoader(List<URL> urls, ClassLoader parent, IAddonManager addonManager) {
        super(urls.toArray(new URL[0]), parent);
        this.addonManager = addonManager;
    }

    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> clazz = super.loadClass(name, resolve);
        addonManager.registerInstruction(clazz);
        if(IBranchFunctionDef.class.isAssignableFrom(clazz)) {
            try {
                //Try to register any branch functions if we can
                addonManager.registerBranchFunction((IBranchFunctionDef) clazz.getConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException |
                    InvocationTargetException | NoSuchMethodException e) {
                //TODO setup a logging system
                JavaSkript.LOGGER.warn("Failed to automatically register branch function " + clazz.getName());
            }
        }
        if(ISkriptFunctionDef.class.isAssignableFrom(clazz)) {
            try {
                //Try to register any branch functions if we can
                addonManager.registerFunctionDef((ISkriptFunctionDef) clazz.getConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException |
                    InvocationTargetException | NoSuchMethodException e) {
                //TODO setup a logging system
                JavaSkript.LOGGER.warn("Failed to automatically register branch function " + clazz.getName());
            }
        }
        return clazz;
    }
}
