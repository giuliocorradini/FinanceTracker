package financetracker.gui.element;

import financetracker.gui.model.RecordTableModel;
import javafx.util.Callback;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Formatter;
import java.util.Optional;

public class StylingDoubleTextTableCell extends StylingTextTableCell<RecordTableModel, Double> {
    /**
     * Constructor
     */
    public StylingDoubleTextTableCell() {
        super();
        setConverter(new FailableDoubleStringConverter());
    }
}
