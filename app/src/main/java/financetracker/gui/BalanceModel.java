package financetracker.gui;

import financetracker.*;
import financetracker.Record;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.function.Supplier;
import java.util.stream.Stream;

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

    public BalanceModel(Balance data) {
        this.dao = data;
        this.records = FXCollections.observableArrayList(
                this.dao.stream().map(RecordTableModel::new).toList()
        );

        this.periodFilter = new SimpleObjectProperty<>(PeriodFilter.ALL);

        Summary s = this.getBalanceSummary();

        this.income = new SimpleDoubleProperty(s.income());
        this.outcome = new SimpleDoubleProperty(s.outcome());
        this.flow = new SimpleDoubleProperty(s.flow());


        this.records.addListener((ListChangeListener<RecordTableModel>)c -> updateSummary());
    }

    private Stream<Record> getFilteredElementsFromDAO() {
        Stream<Record> elements = this.dao.stream();

        PeriodFilter currentPeriodFilter = getPeriodFilter();

        if (currentPeriodFilter != PeriodFilter.ALL) {
            elements = BalanceFilter.filterByDateStream(elements, currentPeriodFilter.getStartDate(), currentPeriodFilter.getEndDate());
        }

        return elements;
    }

    /*
     * @return the algebraic sum of all the currently displayed records
     */
    private static double getRecordFlow(Stream<Record> s) {
        return s
                .map(Record::getAmount)
                .reduce(Double::sum)
                .orElse(0.0);
    }

    private static double getRecordIncomeSum(Stream<Record> s) {
        return s
                .map(Record::getAmount)
                .filter(x -> x > 0)
                .reduce(Double::sum)
                .orElse(0.0);
    }

    private static double getRecordOutcomeSum(Stream<Record> s) {
        return s
                .map(Record::getAmount)
                .filter(x -> x < 0)
                .reduce(Double::sum)
                .orElse(0.0);
    }

    public Summary getBalanceSummary() {
        Supplier<Stream<Record>> s = () -> getFilteredElementsFromDAO();

        return new Summary(
                getRecordIncomeSum(s.get()),
                getRecordOutcomeSum(s.get()),
                getRecordFlow(s.get())
        );
    }

    private void updateSummary() {
        Summary s = this.getBalanceSummary();

        this.setIncome(s.income());
        this.setOutcome(s.outcome());
        this.setFlow(s.flow());
    }

    private void updateModelFromDAO() {
        Stream<Record> elements = getFilteredElementsFromDAO();

        this.records.setAll(
                elements.map(RecordTableModel::new).toList()
        );  //TODO: profile memory consumption
    }

    public void addRecord(double amount, String reason, LocalDate date) {
        //Commit data to DAO
        Record r = new Record(amount, reason, date);
        this.dao.addRecord(r);

        PeriodFilter f = getPeriodFilter();
        if(f != PeriodFilter.ALL && !BalanceFilter.isRecordBetween(r, f.getStartDate(), f.getEndDate()))
            this.setPeriodFilter(PeriodFilter.ALL);

        //Retrieve updated data from DAO
        updateModelFromDAO();
    }

    public void replaceDAO(Balance b) {
        this.dao = b;
        updateModelFromDAO();
    }

    public void deleteRecord(RecordTableModel r) {
        this.dao.deleteRecord(r.getRecord());
        updateModelFromDAO();
    }

    public void deleteRecords(Stream<RecordTableModel> records) {
        records.forEach(r -> this.dao.deleteRecord(r.getRecord()));
        updateModelFromDAO();
    }

    /*
     * Sets the current filter and updates the model, by requesting the DAO for filtered objects.
     */
    public void filterRecords(PeriodFilter f) {
        setPeriodFilter(f);

        updateModelFromDAO();
    }

    public void resetFilter() {
        filterRecords(PeriodFilter.ALL);
    }
}
