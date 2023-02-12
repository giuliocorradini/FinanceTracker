package financetracker.gui;

import financetracker.Balance;
import financetracker.Record;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BalanceModel {
    private Balance dao;

    private ObservableList<Record> records;

    public ObservableList<Record> getRecords() {
        return records;
    }

    public BalanceModel(Balance data) {
        this.dao = data;
        this.records = FXCollections.observableList(this.dao.getRecordList());
    }
}
