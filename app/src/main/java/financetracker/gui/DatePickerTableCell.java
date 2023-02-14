package financetracker.gui;

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

public class DatePickerTableCell<T> extends TableCell<T, LocalDate> {
    private final DatePicker picker;

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

    @Override
    public void startEdit() {
        super.startEdit();
        picker.setValue(getDate());
    }

    private LocalDate getDate() {
        if (getText() == null)
            return LocalDate.now();
        return LocalDate.parse(getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}