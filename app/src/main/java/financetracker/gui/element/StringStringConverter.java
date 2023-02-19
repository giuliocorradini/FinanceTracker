package financetracker.gui.element;

import javafx.util.StringConverter;

/**
 * A dummy string-to-string converter.
 */
class StringStringConverter extends StringConverter<String> {
    @Override
    public String toString(String object) {
        return object;
    }

    @Override
    public String fromString(String string) {
        return string;
    }
}
