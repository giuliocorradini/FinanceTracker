package financetracker.gui.controller;

import financetracker.gui.model.BalanceModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Locale;


public class AddRecordDialogController implements ModelInjectable {
    private BalanceModel model;

    @FXML private TextField reason;
    @FXML private TextField amount;
    @FXML private DatePicker date;
    @FXML private Pane addRecordDialog; //the view controlled by this

    @FXML public void initialize() {
        addRecordDialog.visibleProperty().addListener((prop, ov, nv) -> {
            if(nv) {
                reason.clear();
                amount.clear();
                amount.getStyleClass().remove("error");
                date.setValue(LocalDate.now());
                date.getStyleClass().remove("error");
            }
        });
    }

    public void setModel(BalanceModel m) {
        this.model = m;
    }

    private void closeThisDialog() {
        addRecordDialog.setVisible(false);
        addRecordDialog.setManaged(false);
    }

    @FXML protected void handleSubmit(ActionEvent evt) {
        NumberFormat fmt = NumberFormat.getInstance(Locale.getDefault());

        try {
            this.model.addRecord(
                    fmt.parse(amount.getText()).doubleValue(),
                    reason.getText(),
                    date.getValue()
            );

            closeThisDialog();
        } catch (ParseException e) {
            Dialog<String> dialog = new Dialog<>();
            dialog.setContentText(String.format("\"%s\" is an invalid amount of money.", amount.getText()));
            dialog.getDialogPane().getButtonTypes().add(new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE));

            amount.getStyleClass().add("error");

            dialog.showAndWait();
        } catch (IllegalArgumentException e) {
            Dialog<String> dialog = new Dialog<>();
            dialog.setContentText("Date field can't be null");
            dialog.getDialogPane().getButtonTypes().add(new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE));

            date.getStyleClass().add("error");

            dialog.showAndWait();
        }
    }

    @FXML protected void handleCancel(ActionEvent e) {
        closeThisDialog();
    }

    public void setIncome() {
        String user_input = amount.getText();

        amount.setText(user_input.replaceFirst("^-", ""));
    }

    public void setOutcome() {
        String user_input = amount.getText();

        user_input = user_input.replaceFirst("^-", "");
        amount.setText("-" + user_input);
    }
}
