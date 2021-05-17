package com.bigbade.javaskript.parser.api;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This must be annotated over whatever method is used.
 * Any arguments in the patterns should be arguments in the method annotated by this.
 * If the pattern data is not null (either every case must be null or none), the pattern data type
 * should be the first parameter.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public @interface SkriptStatementMethod { }
