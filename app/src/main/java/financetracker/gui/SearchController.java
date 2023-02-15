package financetracker.gui;

import financetracker.BalanceSearchEngine;
import financetracker.ControllerInjectable;
import financetracker.ModelInjectable;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import financetracker.Record;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.List;


public class SearchController implements ModelInjectable, ControllerInjectable {
    private BalanceModel model;
    private AppController ac;
    private List<Record> results;
    private int currentIndex;
    @FXML private TextField searchField;
    @FXML private Pane root;

    @Override
    public void setModel(BalanceModel b) {
        this.model = b;
    }

    private void performSearch() {
        BalanceSearchEngine engine = new BalanceSearchEngine(this.model.getDao());
        String query = searchField.getText();

        this.results = engine.search(query).toList();
        this.currentIndex = 0;
    }

    private void showResult() {
        if(results.size() > 0) {
            Record r = results.get(currentIndex);
            this.ac.highlightRecord(r);
        }
    }

    @FXML protected void highlightPrevious() {
        if(this.results == null)
            performSearch();

        currentIndex--;
        if(currentIndex < 0)
            currentIndex = results.size() - 1;

        showResult();
    }

    @FXML protected void highlightNext() {
        if(this.results == null)
            performSearch();

        currentIndex++;
        if(currentIndex >= results.size())
            currentIndex = 0;

        showResult();
    }

    @FXML protected void handleSearch() {
        performSearch();

        showResult();
    }

    private void invalidateSearch() {
        this.results = null;
        this.currentIndex = 0;
    }

    public void setController(AppController c) {
        this.ac = c;
    }
}
