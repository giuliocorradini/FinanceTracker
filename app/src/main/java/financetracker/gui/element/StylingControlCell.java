package financetracker.gui.element;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Control;
import javafx.scene.control.TableCell;

/**
 * This class represents a table cell that shows a control and styles itself
 * if an error arises while parsing.
 *
 * <p>
 *     This class provides an {@link #errorProperty()} that is bound to the set/reset
 *     of a cell's style. The inheriting class must call setError when appropriate.
 *     This class automatically resets the error style when focus is lost.
 * </p>
 *
 * <p>
 *     The parsing follows a sort of Execute Around pattern, as it is responsibility
 *     of the overriding class to provide a converter and use it to set the error property accordingly.
 * </p>
 * @param <S> the row model.
 * @param <T> the column field type
 */
public class StylingControlCell<S, T> extends TableCell<S, T> {
    private Control control;
    private FailableStringConverter<T> converter;

    private BooleanProperty error;
    public boolean isError() {
        return error.get();
    }
    public BooleanProperty errorProperty() {
        return error;
    }
    public void setError(boolean error) {
        this.error.set(error);
    }


    /**
     * Constructor
     */
    public StylingControlCell() {
        super();
        error = new SimpleBooleanProperty(false);

        error.addListener(
                (obs, ov, nv) -> {
                    if(nv == true)
                        getStyleClass().add("error");
                    else
                        getStyleClass().remove("error");
                }
        );

        contentDisplayProperty().bind(
                Bindings.when(editingProperty())
                        .then(ContentDisplay.GRAPHIC_ONLY)
                        .otherwise(ContentDisplay.TEXT_ONLY)
        );

        focusWithinProperty().addListener(
                (obs, ov, nv) -> {
                    if(nv == false)
                        setError(false);
                }
        );
    }

    /**
     * Sets the control displayed during edits. It's responsibility of the
     * overriding class to call this method.
     * @param c the control to set
     */
    public void setControl(Control c) {
        this.control = c;
    }

    /**
     * Sets the converter from String to T and viceversa.
     * @param conv the converter
     */
    public void setConverter(FailableStringConverter<T> conv) {
        converter = conv;
    }

    /**
     * Sets the graphic (text or control) for the cell
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
            setGraphic(this.control);
            setText(converter.toString(item));
        }
    }
}
