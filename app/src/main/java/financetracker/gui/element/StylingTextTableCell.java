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
public class StylingTextTableCell<S, T> extends TableCell<S, T> {
    private final TextField field;
    private FailableStringConverter<T> converter;

    /**
     * Constructor
     */
    public StylingTextTableCell() {
        super();
        field = new TextField();

        field.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                try {
                    T val = converter.fromStringWithException(field.getText());
                    commitEdit(val);
                    getStyleClass().remove("error");
                } catch (ParseException e) {
                    getStyleClass().add("error");
                }
            }

            if (event.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });

        contentDisplayProperty().bind(
                Bindings.when(editingProperty())
                        .then(ContentDisplay.GRAPHIC_ONLY)
                        .otherwise(ContentDisplay.TEXT_ONLY)
        );

        focusWithinProperty().addListener(
                (obs, ov, nv) -> {
                    if(nv == false)
                        getStyleClass().remove("error");
                }
        );
    }

    /**
     * Sets the converter from String to T and viceversa.
     * @param conv the converter
     */
    public void setConverter(FailableStringConverter<T> conv) {
        converter = conv;
    }

    /**
     * Try to parse item and convert it to a value. If the parsing is not successful, an "error"
     * style is applied to the node.
     * @param item The new item for the cell.
     * @param empty whether this cell represents data from the list. If it
     *        is empty, then it does not represent any domain data, but is a cell
     *        being used to render an "empty" row.
     */
    @Override
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setText(null);
            setGraphic(null);
        } else {
            setGraphic(this.field);
            setText(converter.toString(item));
        }
    }

    @Override
    public void startEdit() {
        super.startEdit();
        field.setText(converter.toString(getItem()));
    }
}
