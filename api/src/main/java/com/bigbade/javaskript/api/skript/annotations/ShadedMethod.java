package com.bigbade.javaskript.api.skript.annotations;

import com.bigbade.javaskript.api.skript.addon.ISkriptFunctionDef;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This must be annotated over whatever method is used.
 * Any arguments in the patterns should be arguments in the method annotated by this.
 * Any referenced methods/fields/etc... will be shaded in also, and references
 * to the key value map or pattern data will be replaced with the value.
 * Calls to ICodeDef#execute will be replaced by the code in the block
 * @see com.bigbade.javaskript.api.skript.defs.ICodeDef
 * @see ISkriptFunctionDef
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@SupportedSourceVersion(SourceVersion.RELEASE_16)
public @interface ShadedMethod {
}
