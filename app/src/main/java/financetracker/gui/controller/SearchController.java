package financetracker.gui.controller;

import financetracker.BalanceSearchEngine;
import financetracker.gui.model.BalanceModel;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import financetracker.Record;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public class SearchController implements ModelInjectable, ControllerInjectable {
    private BalanceModel model;
    private AppController ac;
    private List<Record> results;
    private int currentIndex;
    @FXML private TextField searchField;
    @FXML private Pane root;
    @FXML private CheckBox useRegex;
    @FXML private Pane searchFieldGroup;

    @Override
    public void setModel(BalanceModel b) {
        this.model = b;
    }

    private void performSearch() {
        BalanceSearchEngine engine = new BalanceSearchEngine(this.model.getDao());
        String query = searchField.getText();

        Pattern q;

        try {
            if(useRegex.isSelected())
                q = Pattern.compile(query);
            else
                q = Pattern.compile(Pattern.quote(query));

            searchFieldGroup.getChildren().stream().forEach(n -> n.getStyleClass().remove("error"));
            this.results = engine.search(q).toList();
            this.currentIndex = 0;
        } catch (PatternSyntaxException e) {
            searchFieldGroup.getChildren().stream().forEach(n -> n.getStyleClass().add("error"));
        }
    }

    private void showResult() {
        if(this.results != null && results.size() > 0) {
            Record r = results.get(currentIndex);
            this.ac.highlightRecord(r);
        } else {
            this.ac.highlightRecord(null);
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

    public void setController(AppController c) {
        this.ac = c;
    }
}
