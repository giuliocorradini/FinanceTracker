package financetracker.gui;

import financetracker.Balance;
import financetracker.Record;
import financetracker.Summary;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BalanceModel {
    private Balance dao;

    private ObservableList<Record> records;
    public ObservableList<Record> getRecords() {
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
        this.records = FXCollections.observableList(this.dao.getRecordList());

        Summary s = this.dao.getBalanceSummary();

        this.income = new SimpleDoubleProperty(s.income());
        this.outcome = new SimpleDoubleProperty(s.outcome());
        this.flow = new SimpleDoubleProperty(s.flow());
    }
}
