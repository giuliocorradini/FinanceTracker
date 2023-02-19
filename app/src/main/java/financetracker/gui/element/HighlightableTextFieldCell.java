package financetracker.gui.element;

import financetracker.gui.model.RecordTableModel;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;

/**
 * A text field cell that can be highlighted if the {@link RecordTableModel#highlightedProperty()}
 * is set.
 */
public class HighlightableTextFieldCell extends TextFieldTableCell<RecordTableModel, String> {
    /**
     * Constructor. Sets a simple String to String converter, required by the superclass.
     */
    public HighlightableTextFieldCell() {
        super();
        setConverter(new StringStringConverter());

    }

    /**
     * Updates the cell look when the row content changes. Sets the according style class if
     * the highlighted property is set; removes it otherwise.
     * @param content The new item for the cell.
     * @param empty whether this cell represents data from the list. If it
     *        is empty, then it does not represent any domain data, but is a cell
     *        being used to render an "empty" row.
     */
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
