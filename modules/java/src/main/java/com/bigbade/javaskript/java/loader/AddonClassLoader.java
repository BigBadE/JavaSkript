package com.bigbade.javaskript.java.loader;

import com.bigbade.javaskript.api.skript.addon.IAddonManager;

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
        //TODO register types
        return clazz;
    }
}
