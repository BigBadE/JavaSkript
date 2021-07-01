package com.bigbade.javaskript.api.skript.defs;

import com.bigbade.javaskript.api.skript.pattern.ILineParser;

/**
 * Translates a String value provided by the coder into T.
 * @param <T> Type to be translated to
 */
@SuppressWarnings("unused")
public interface IValueTranslator<T> {
    /**
     * Translates the parsed line to the value.
     * @return Output value
     */
    T getValue();

    /**
     * Reads the line
     * @param lineParser Line parser
     * @param lineNumber Line number
     * @param line Line to read
     */
    void readLine(ILineParser lineParser, int lineNumber, String line);

    /**
     * Indicates the start of a branch function (line ending with a colon that branches the control flow of the method).
     * Will not call readLine on the line.
     * @param lineParser Line parser
     * @param lineNumber Line number
     * @param line Line to read
     */
    void startBranchFunction(ILineParser lineParser, int lineNumber, String line);

    /**
     * Indicates the end of a function by a change in spacing. Will call readLine after.
     * @param lineParser Line parser
     * @param lineNumber Line number
     */
    void endBranchFunction(ILineParser lineParser, int lineNumber);

    /**
     * Returns true if the first line of the value is read, false for multi-line translators like code
     * @return True if single line, false if not
     */
    boolean readsFirstLine();
}
