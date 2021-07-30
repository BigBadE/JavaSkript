package com.bigbade.javaskript.java;

import com.bigbade.javaskript.api.skript.parser.ISkriptParser;
import com.bigbade.javaskript.java.loader.AddonClassLoader;
import com.bigbade.javaskript.parser.SkriptParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.*;

public class JavaSkript {
    public static final Logger LOGGER = LoggerFactory.getLogger(JavaSkript.class);

    public static void main(String[] args) {
        ISkriptParser parser = new SkriptParser();
        List<URL> urls = new ArrayList<>();
        AddonClassLoader addonClassLoader = new AddonClassLoader(urls, JavaSkript.class.getClassLoader(),
                parser.getLineParser().getAddonManager());

        //Class loaders and the parser are both thread-safe, so the jars should be loaded async.
        ExecutorService executor = Executors.newCachedThreadPool();
        loadSkriptClasses(addonClassLoader, executor);
    }

    public static void loadSkriptClasses(ClassLoader classLoader, ExecutorService executor) {
        try {
            executor.submit(() -> {
                try {
                    loadClasses(classLoader);
                } catch (ClassNotFoundException | IOException e) {
                    LOGGER.error("Error loading Skript", e);
                }
            }).get(2, TimeUnit.MINUTES);
            executor.shutdown();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException | TimeoutException e) {
            LOGGER.error("Error loading Skript", e);
        }
    }

    /**
     * Loads all classes in the jar
     * @param classLoader Class loader to load with
     * @throws ClassNotFoundException If the class can't be loaded
     * @throws IOException If there is a problem reading the jar
     * @author Victor Tatai from https://dzone.com/articles/get-all-classes-within-package
     */
    private static void loadClasses(ClassLoader classLoader)
            throws ClassNotFoundException, IOException {
        assert classLoader != null;
        String path = "com/bigbade/javaskript/java";
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        for (File directory : dirs) {
            findClasses(classLoader, directory, "com.bigbade.javaskript.java");
        }
    }

    /**
     * Recursively finds and loads all classes in the directory
     * @param classLoader Class loader to load with
     * @param directory Directory to load from
     * @param packageName Name of the package
     * @throws ClassNotFoundException If the class can't be loaded
     */
    private static void findClasses(ClassLoader classLoader, File directory, String packageName) throws ClassNotFoundException {
        if (!directory.exists()) {
            return;
        }
        File[] files = directory.listFiles();
        assert files != null;
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                findClasses(classLoader, file, packageName + "." + file.getName());
            } else if (file.getName().endsWith(".class")) {
                classLoader.loadClass(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
            }
        }
    }
}
