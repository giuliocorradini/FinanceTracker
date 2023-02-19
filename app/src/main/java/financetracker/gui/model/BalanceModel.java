package financetracker.gui.model;

import financetracker.*;
import financetracker.Record;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * This class represents the model of the MVC pattern. It wraps a Balance, that acts
 * as the data access object.
 *
 * <p>
 *     Every field of Balance is exposed as an Observable property, hence GUI controls can
 *     bind to these properties and update whenever a change is made.
 * </p>
 */
public class BalanceModel {
    private Balance dao;
    public Balance getDao() {
        return dao;
    }

    private ObservableList<RecordTableModel> records;
    public ObservableList<RecordTableModel> getRecords() {
        return records;
    }

    private DoubleProperty income;
    public double getIncome() {
        return income.get();
    }
    public DoubleProperty incomeProperty() {
        return income;
    }
    public void setIncome(double income) {
        this.income.set(income);
    }

    private DoubleProperty outcome;
    public double getOutcome() {
        return outcome.get();
    }
    public DoubleProperty outcomeProperty() {
        return outcome;
    }
    public void setOutcome(double outcome) {
        this.outcome.set(outcome);
    }

    private DoubleProperty flow;
    public double getFlow() {
        return flow.get();
    }
    public DoubleProperty flowProperty() {
        return flow;
    }
    public void setFlow(double flow) {
        this.flow.set(flow);
    }

    private Property<PeriodFilter> periodFilter;
    public PeriodFilter getPeriodFilter() {
        return periodFilter.getValue();
    }
    public Property<PeriodFilter> periodFilterProperty() {
        return periodFilter;
    }
    public void setPeriodFilter(PeriodFilter pf) {
        this.periodFilter.setValue(pf);
    }

    private BooleanProperty edited;
    public boolean isEdited() {
        return edited.get();
    }

    /**
     * Represents if the model has been edited and not been saved.
     */
    public BooleanProperty editedProperty() {
        return edited;
    }
    public void setEdited(boolean edited) {
        this.edited.set(edited);
    }

    /**
     * Constructor.
     * @param data the data access object to encapsulate.
     */
    public BalanceModel(Balance data) {
        this.dao = data;
        this.records = FXCollections.observableArrayList();
        this.periodFilter = new SimpleObjectProperty<>(PeriodFilter.ALL());
        this.updateModelFromDAO();

        Summary s = this.getBalanceSummary();

        this.income = new SimpleDoubleProperty(s.income());
        this.outcome = new SimpleDoubleProperty(s.outcome());
        this.flow = new SimpleDoubleProperty(s.flow());

        this.edited = new SimpleBooleanProperty(false);

        this.records.addListener((ListChangeListener<RecordTableModel>)c -> updateSummary());
    }

    /**
     * Get the Records saved in the underlying Balance, filters with the currently selected filter.
     * @return a stream with the filtered elements
     */
    private Stream<Record> getFilteredElementsFromDAO() {
        Stream<Record> elements = this.dao.stream();

        PeriodFilter currentPeriodFilter = getPeriodFilter();

        if (!currentPeriodFilter.all()) {
            elements = BalanceFilter.filterByDateStream(elements, currentPeriodFilter.startDate(), currentPeriodFilter.endDate());
        }

        return elements;
    }

    /**
     * Compute the algebraic sum of the amounts in a stream of records.
     * @param s a stream of records, if you want to compute the flow for filtered elements,
     *          use {@link #getFilteredElementsFromDAO()} first.
     * @return the algebraic sum of the record amounts
     */
    private static double getRecordFlow(Stream<Record> s) {
        return s
                .map(Record::getAmount)
                .reduce(Double::sum)
                .orElse(0.0);
    }

    /**
     * Compute the algebraic sum of the positive amounts only.
     * @param s a stream of records.
     * @return the algebraic sum of the positive amounts.
     */
    private static double getRecordIncomeSum(Stream<Record> s) {
        return s
                .map(Record::getAmount)
                .filter(x -> x > 0)
                .reduce(Double::sum)
                .orElse(0.0);
    }

    /**
     * Compute the algebraic sum of the negative amounts only.
     * @param s a stream of records.
     * @return the algebraic sum of the negative amounts.
     */
    private static double getRecordOutcomeSum(Stream<Record> s) {
        return s
                .map(Record::getAmount)
                .filter(x -> x < 0)
                .reduce(Double::sum)
                .orElse(0.0);
    }

    /**
     * Compute the summary for currently displayed record.
     * Gets the filtered elements from the DAO, and calls the getRecord methods.
     * @return a Summary with income, outcome and flow for the currently displayed elements.
     */
    public Summary getBalanceSummary() {
        Supplier<Stream<Record>> s = () -> getFilteredElementsFromDAO();

        return new Summary(
                getRecordIncomeSum(s.get()),
                getRecordOutcomeSum(s.get()),
                getRecordFlow(s.get())
        );
    }

    /**
     * Compute the summary and set the appropriate properties to update the controls.
     */
    private void updateSummary() {
        Summary s = this.getBalanceSummary();

        this.setIncome(s.income());
        this.setOutcome(s.outcome());
        this.setFlow(s.flow());
    }

    /**
     * Get the saved records from DAO and sets them in elements observable list
     * (that is used by {@link financetracker.gui.controller.AppController#recordTable}).
     *
     * This automatically refresh the bound controls.
     */
    private void updateModelFromDAO() {
        Stream<Record> elements = getFilteredElementsFromDAO();

        this.records.setAll(
                elements.map(r -> new RecordTableModel(this, r)).toList()
        );  //TODO: profile memory consumption
    }

    /**
     * Adds a record to the underlying Balance, and refresh the bound controls.
     *
     * Commits new record to the DAO and queries the updated data.
     * @param amount a money amount
     * @param reason reason of the transaction
     * @param date date of the transaction
     */
    public void addRecord(double amount, String reason, LocalDate date) {
        //Commit data to DAO
        Record r = new Record(amount, reason, date);
        this.dao.addRecord(r);

        PeriodFilter f = getPeriodFilter();
        if(!f.all() && !BalanceFilter.isRecordBetween(r, f.startDate(), f.endDate()))
            this.setPeriodFilter(PeriodFilter.ALL());

        //Retrieve updated data from DAO
        updateModelFromDAO();

        setEdited(true);
    }

    /**
     * Replace the underlying Balance. Use this when opening a new file.
     * A query to the DAO is immediately executed in order to refresh the controls bound
     * to this model properties.
     * @param b the new balance.
     */
    public synchronized void replaceDAO(Balance b) {
        if(b != null) {
            this.dao = b;
            updateModelFromDAO();
        }

        setEdited(false);
    }

    /**
     * Deletes a row from the Balance and updates the view.
     *
     * Deletion policy is specified in {@link Balance#deleteRecord(Record)}.
     * @param r a row to delete, where a Record is extracted.
     */
    public void deleteRecord(RecordTableModel r) {
        this.dao.deleteRecord(r.getRecord());
        updateModelFromDAO();
        setEdited(true);
    }

    /**
     * Deletes a stream of records.
     * @param records a stream of records.
     */
    public void deleteRecords(Stream<RecordTableModel> records) {
        records.forEach(r -> this.dao.deleteRecord(r.getRecord()));
        updateModelFromDAO();
        setEdited(true);
    }

    /**
     * Sets the current filter and updates the model, by requesting the DAO for filtered objects.
     * @param f a PeriodFilter representing the time window.
     */
    public void filterRecords(PeriodFilter f) {
        setPeriodFilter(f);

        updateModelFromDAO();
    }

    /**
     * Sets the period filter to ALL
     */
    public void resetFilter() {
        setPeriodFilter(PeriodFilter.ALL());
    }

    /**
     * Reset the underlying DAO, by replacing it with a new Balance; and resets the period filter.
     */
    public void resetAll() {
        this.replaceDAO(new Balance());
        this.resetFilter();
    }

    /**
     * Updates the underlying record, with the values in RecordTableModel.
     * If needed updates the summary properties.
     * @param r a row
     */
    public void editRecord(RecordTableModel r) {
        Record ur = r.getRecord();
        boolean updateSum = (ur.getAmount() != r.getAmount());

        ur.setDate(r.getDate());
        ur.setReason(r.getReason());
        ur.setAmount(r.getAmount());

        if(updateSum)
            updateSummary();

        setEdited(true);
    }
}
