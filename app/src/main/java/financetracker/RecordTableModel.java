package financetracker;

import javafx.beans.property.*;

import java.time.LocalDate;

public class RecordTableModel {
    private Record record;

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
    }


    public RecordTableModel(Record r) {
        this.record = r;

        this.selected = new SimpleBooleanProperty(false);
        this.amount = new SimpleDoubleProperty(r.getAmount());
        this.reason = new SimpleStringProperty(r.getReason());
        this.date = new SimpleObjectProperty<LocalDate>(r.getDate());
    }

    public Record getRecord() {
        return this.record;
    }
}
