package com.bigbade.javaskript.api.skript.annotations;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This must be annotated over whatever method is used.
 * Any arguments in the patterns should be arguments in the method annotated by this.
 * Any integer parameter named "patternData" will be replaced with the actual patternData,
 * Some addons will add their own parameter overrides (Like Bukkit for JavaPlugin).
 */
@Repeatable(SkriptPattern.SkriptPatterns.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@SupportedSourceVersion(SourceVersion.RELEASE_16)
public @interface SkriptPattern {
    /**
     * Pattern of this method
     * @return Pattern for this object
     */
    String pattern();

    /**
     * Sadly a user-supplied Enum cannot be used here, so instead it returns a number.
     * @return Pattern data applied to this pattern
     */
    int patternData() default -1;

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @SupportedSourceVersion(SourceVersion.RELEASE_8)
    @interface SkriptPatterns {
        SkriptPattern[] value();
    }
}

