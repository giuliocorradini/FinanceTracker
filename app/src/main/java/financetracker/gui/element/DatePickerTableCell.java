package financetracker.gui.element;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.beans.binding.Bindings;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * A cell that shows a date, and a DatePicker control when editing.
 * @param <T> the class representing a table row.
 */
public class DatePickerTableCell<T> extends TableCell<T, LocalDate> {
    private final DatePicker picker;

    /**
     * Constrcutor.
     * Build the underlying DatePicker that is shown only when editing. The control is then hidden
     * and data commited with a double-click on a day in the calendar, or a keypress.
     */
    public DatePickerTableCell() {
        picker = new DatePicker();
        picker.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                picker.setValue(picker.getConverter().fromString(picker.getEditor().getText()));
                commitEdit(picker.getValue());
            }

            if (event.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });

        picker.setDayCellFactory(picker -> {
            DateCell cell = new DateCell();

            cell.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                this.picker.setValue(cell.getItem());
                if (event.getClickCount() == 2) {
                    this.picker.hide();
                    commitEdit(cell.getItem());
                }
                event.consume();
            });

            return cell;
        });

        contentDisplayProperty().bind(
                Bindings.when(editingProperty())
                        .then(ContentDisplay.GRAPHIC_ONLY)
                        .otherwise(ContentDisplay.TEXT_ONLY)
        );
    }

    /**
     * Sets the graphic for the cell: a simple string with the date if the user is not
     * editing, otherwise shows the DatePicker control.
     * @param date The new item for the cell.
     * @param empty whether or not this cell represents data from the list. If it
     *        is empty, then it does not represent any domain data, but is a cell
     *        being used to render an "empty" row.
     */
    @Override
    public void updateItem(LocalDate date, boolean empty) {
        super.updateItem(date, empty);
        if (date == null || empty) {
            setText(null);
            setGraphic(null);
        } else {
            setGraphic(this.picker);
            setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
    }

    /**
     * Called by superclass when the user wants to edit the cell. Gets the current value
     * and pass it to the picker control.
     */
    @Override
    public void startEdit() {
        super.startEdit();
        picker.setValue(getDate());
    }

    /**
     * Parse the current text in DatePicker and attempt to convert it to a date.
     * @return a date
     */
    private LocalDate getDate() {
        if (getText() == null)
            return LocalDate.now();
        return LocalDate.parse(getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}