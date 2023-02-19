package financetracker.gui.element;

import financetracker.gui.model.RecordTableModel;

/**
 * A simple styling text field cell that holds and parses double values.
 */
public class StylingDoubleFieldCell extends StylingTextFieldCell<RecordTableModel, Double> {
    /**
     * Constructor
     */
    public StylingDoubleFieldCell() {
        super();
        setConverter(new FailableDoubleStringConverter());
    }
}
