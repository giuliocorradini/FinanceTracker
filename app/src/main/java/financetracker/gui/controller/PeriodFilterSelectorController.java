package financetracker.gui.controller;

import financetracker.gui.model.PeriodFilter;
import financetracker.gui.model.BalanceModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * Controller for the custom period selector window.
 */
public class PeriodFilterSelectorController implements ModelInjectable {
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private Pane rootPane;
    private BalanceModel model;

    @FXML public void initialize() {
        cleanUp();
    }

    @Override
    public void setModel(BalanceModel m) {
        this.model = m;
    }

    /**
     * Clean the control values.
     */
    private void cleanUp() {
        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(LocalDate.now());
    }

    /**
     * Close the associated window with the controlled view.
     */
    private void closeThisWindow() {
        Stage s = (Stage) rootPane.getScene().getWindow();
        s.close();
    }

    /**
     * Handle the action for "Cancel" button.
     */
    @FXML protected void handleCancel() {
        cleanUp();
        closeThisWindow();
    }

    /**
     * Handle the action for the "Confirm" button.
     * If the specified period is not valid, shows an alert.
     */
    @FXML protected void handleConfirm() {
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();

        if(start.isAfter(end)) {
            Alert a = new Alert(Alert.AlertType.ERROR, "Start date can't be after the end date.", ButtonType.OK);
            a.showAndWait();
        } else {
            PeriodFilter f = new PeriodFilter(start, end);
            this.model.filterRecords(f);
            closeThisWindow();
        }
    }

    /**
     * Removes the specified amount of the button from the startDate.
     * @param evt an ActionEvent that wraps the caller button
     */
    public void handleQuickPeriod(ActionEvent evt) {
        Button src = (Button) evt.getSource();

        switch(src.getText()) {
            case "1 day" -> startDatePicker.setValue(endDatePicker.getValue().minusDays(1));
            case "1 week" -> startDatePicker.setValue(endDatePicker.getValue().minusWeeks(1));
            case "1 month" -> startDatePicker.setValue(endDatePicker.getValue().minusMonths(1));
            case "1 year" -> startDatePicker.setValue(endDatePicker.getValue().minusYears(1));
        }
    }
}
