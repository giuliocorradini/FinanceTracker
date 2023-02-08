package financetracker.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;


public class AddRecordDialog {
    @FXML private TextField reason;
    @FXML private TextField amount;
    @FXML private DatePicker date;

    private void closeThisDialog() {
        StageManager.getStageManager().restorePreviousScene();
    }

    @FXML protected void handleSubmit(ActionEvent e) {
        System.out.println("User added new record with: " + reason.getText() + " " + amount.getText() + " " + date.getValue());

        closeThisDialog();
    }

    @FXML protected void handleCancel(ActionEvent e) {
        closeThisDialog();
    }
}
