package financetracker.gui.element;

import java.text.ParseException;
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
 * If date parsing arises an error, styles itself according to the "error" style class.
 * @param <T> the class representing a table row.
 */
public class DatePickerTableCell<T> extends StylingControlCell<T, LocalDate> {
    private final DatePicker picker;
    private final FailableDateConverter converter;

    /**
     * Constructor.
     * Build the underlying DatePicker that is shown only when editing. The control is then hidden
     * and data committed with a double click on a day in the calendar, or a keypress.
     */
    public DatePickerTableCell() {
        super();
        picker = new DatePicker();
        converter = new FailableDateConverter();

        setConverter(converter);
        setControl(picker);

        picker.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                try {
                    picker.setValue(
                            converter.fromStringWithException(picker.getEditor().getText())
                    );
                    commitEdit(picker.getValue());
                    setError(false);
                } catch (ParseException e) {
                    setError(true);
                }
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
     * But doesn't throw an exception.
     * @return a date
     */
    private LocalDate getDate() {
        if (getText() == null)
            return LocalDate.now();
        return converter.fromString(getText());
    }
}