package com.bigbade.javaskript.api.skript.annotations;


import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This must be annotated over whatever class is used for a function.
 */
@Repeatable(FunctionPattern.FunctionPatterns.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FunctionPattern {
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
    @Target(ElementType.TYPE)
    @SupportedSourceVersion(SourceVersion.RELEASE_8)
    @interface FunctionPatterns {
        FunctionPattern[] value();
    }
}
