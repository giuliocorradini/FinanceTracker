package financetracker.gui.controller;

import financetracker.gui.model.PeriodFilter;
import financetracker.gui.model.BalanceModel;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * Controller for the custom period selector window.
 */
public class CustomPeriodFilterSelectorController implements ModelInjectable {
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
        startDatePicker.setValue(PeriodFilter.CUSTOM.getStartDate());
        endDatePicker.setValue(PeriodFilter.CUSTOM.getEndDate());
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
            PeriodFilter f = PeriodFilter.CUSTOM;
            f.setStartDate(start);
            f.setEndDate(end);
            this.model.filterRecords(f);
            closeThisWindow();
        }
    }
}
