package financetracker.gui.model;

import financetracker.Record;
import javafx.beans.property.*;

import java.time.LocalDate;

/**
 * This class wraps a Record and is used to represent a row of a TableView.
 */
public class RecordTableModel {
    private Record record;
    private BalanceModel parent;

    private BooleanProperty selected;

    public boolean isSelected() {
        return selected.get();
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }


    private StringProperty reason;

    public String getReason() {
        return reason.get();
    }

    public StringProperty reasonProperty() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason.set(reason);
        this.parent.editRecord(this);
    }


    private DoubleProperty amount;

    public double getAmount() {
        return amount.get();
    }

    public DoubleProperty amountProperty() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
        this.parent.editRecord(this);
    }


    private Property<LocalDate> date;

    public LocalDate getDate() {
        return date.getValue();
    }

    public Property<LocalDate> dateProperty() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date.setValue(date);
        this.parent.editRecord(this);
    }

    private BooleanProperty highlighted;

    public boolean isHighlighted() {
        return highlighted.get();
    }

    public BooleanProperty highlightedProperty() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted.set(highlighted);
    }

    /**
     * Constructor.
     * @param r the record to wrap.
     */
    public RecordTableModel(BalanceModel m, Record r) {
        this.record = r;
        this.parent = m;

        this.selected = new SimpleBooleanProperty(false);
        this.amount = new SimpleDoubleProperty(r.getAmount());
        this.reason = new SimpleStringProperty(r.getReason());
        this.date = new SimpleObjectProperty<LocalDate>(r.getDate());
        this.highlighted = new SimpleBooleanProperty(false);
    }

    /**
     * @return the underlying record.
     */
    public Record getRecord() {
        return this.record;
    }
}
