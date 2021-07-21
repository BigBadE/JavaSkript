package com.bigbade.javaskript.api.skript.parser;

import com.bigbade.javaskript.api.skript.defs.ISkriptFile;
import com.bigbade.javaskript.api.skript.pattern.ILineParser;

import java.io.File;
import java.io.Reader;

/**
 * Parses files into an ISkriptFile
 * @see ISkriptFile
 */
public interface ISkriptParser {
    ILineParser getLineParser();

    ISkriptFile parseSkript(File file);

    ISkriptFile parseSkript(Reader reader, String name);
}
