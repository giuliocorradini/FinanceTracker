package financetracker.gui;

import financetracker.*;
import financetracker.Record;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.stream.Stream;

public class BalanceModel {
    private Balance dao;
    public Balance getDao() {
        return dao;
    }

    private PeriodFilter currentPeriodFilter = PeriodFilter.ALL;

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

    public BalanceModel(Balance data) {
        this.dao = data;
        this.records = FXCollections.observableArrayList(
                this.dao.stream().map(RecordTableModel::new).toList()
        );

        Summary s = this.dao.getBalanceSummary();

        this.income = new SimpleDoubleProperty(s.income());
        this.outcome = new SimpleDoubleProperty(s.outcome());
        this.flow = new SimpleDoubleProperty(s.flow());

        this.records.addListener((ListChangeListener<RecordTableModel>)c -> updateSummary());
    }

    private Stream<Record> getFilteredElementsFromDAO() {
        Stream<Record> elements = this.dao.stream();

        if (currentPeriodFilter != PeriodFilter.ALL) {
            elements = BalanceFilter.filterByDateStream(elements, currentPeriodFilter.getStartDate(), currentPeriodFilter.getEndDate());
        }

        return elements;
    }

    private void updateSummary() {
        Summary s = Balance.getBalanceSummary(
                () -> getFilteredElementsFromDAO()
        );

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
        this.dao.addRecord(amount, reason, date);

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

    public void filterRecords(PeriodFilter f) {
        currentPeriodFilter = f;

        updateModelFromDAO();
    }

    public void resetFilter() {
        filterRecords(PeriodFilter.ALL);
    }
}
