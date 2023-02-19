package financetracker.gui.element;

import javafx.beans.binding.Bindings;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.text.ParseException;


/**
 * This class represents a table cell that holds a value, but styles itself
 * if an error arises while parsing.
 *
 * <p>
 *     The actual conversion is made in the updateItem method
 * </p>
 * @param <S> the row model.
 * @param <T> the column field type
 */
public class StylingTextFieldCell<S, T> extends StylingControlCell<S, T> {
    private final TextField field;
    private FailableStringConverter<T> converter;


    /**
     * Constructor
     */
    public StylingTextFieldCell() {
        super();
        field = new TextField();
        super.setControl(field);

        field.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                try {
                    T val = converter.fromStringWithException(field.getText());
                    commitEdit(val);
                    setError(false);
                } catch (ParseException e) {
                    setError(true);
                }
            }

            if (event.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });
    }

    /**
     * Sets the converter from String to T and viceversa.
     * @param conv the converter
     */
    @Override
    public void setConverter(FailableStringConverter<T> conv) {
        super.setConverter(conv);
        converter = conv;
    }

    @Override
    public void startEdit() {
        super.startEdit();
        field.setText(converter.toString(getItem()));
    }
}
