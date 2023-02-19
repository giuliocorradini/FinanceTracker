package financetracker.gui.element;

import javafx.util.StringConverter;

import java.text.ParseException;

/**
 * An abstract class that extends StringConverter and provides a method that throws
 * an exception if the parsing can't be made.
 * @param <S>
 */
public abstract class FailableStringConverter<S> extends StringConverter<S> {
    /**
     * Attempts to convert from string, but may rise an exception if it can't
     * parse the string.
     * @param string the string to convert from
     * @return the converted object
     * @throws ParseException if the string cannot be converted
     */
    public abstract S fromStringWithException(String string) throws ParseException;

    /**
     * Convert from string to object, but fail silently if a parse exception arises.
     * @param string the {@code String} to convert
     * @return the converted object.
     */
    public S fromString(String string) {
        try {
            return fromStringWithException(string);
        } catch (ParseException e) {
            return null;
        }
    }
}
