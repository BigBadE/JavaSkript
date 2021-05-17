package com.bigbade.javaskript.api.skript.defs;

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
     * @param line Line to read
     */
    void readLine(String line);

    /**
     * Returns true if the first line of the value is read, false for multi-line translators like code
     * @return True if single line, false if not
     */
    boolean readsFirstLine();
}
