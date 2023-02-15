package financetracker.gui;

import financetracker.RecordTableModel;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;

public class HighlightableTextFieldCell extends TextFieldTableCell<RecordTableModel, String> {
    public HighlightableTextFieldCell() {
        super();
        setConverter(new StringConverter<String>() {
            @Override
            public String toString(String object) {
                return object;
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        });

    }

    @Override
    public void updateItem(String content, boolean empty) {
        super.updateItem(content, empty);
        if(!empty && getTableRow().getItem().isHighlighted()) {
            getStyleClass().add("highlight_cell");
        } else {
            getStyleClass().remove("highlight_cell");
        }
    }
}
