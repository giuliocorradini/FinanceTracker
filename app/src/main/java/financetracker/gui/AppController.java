package financetracker.gui;

import financetracker.Balance;
import financetracker.ModelInjectable;
import financetracker.Record;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.time.LocalDate;

public class AppController implements ModelInjectable {

    private BalanceModel model;

    @FXML private Button addButton;
    @FXML private ChoiceBox<String> periodSelector;
    @FXML private TableView<Record> recordTable;
    @FXML private TableColumn<Record, LocalDate> dateColumn;
    @FXML private TableColumn<Record, String> amountColumn;
    @FXML private TableColumn<Record, String> reasonColumn;
    @FXML private TableColumn<Record, Boolean> selectColumn;
    @FXML private Text incomeSummary;
    @FXML private Text outcomeSummary;
    @FXML private Text flowSummary;

    public void setModel(BalanceModel model) {
        this.model = model;
    }

    private Parent loadDialogRoot() {
        Parent dialog_root = null;

        try {
            dialog_root = FXMLLoader.load(getClass().getClassLoader().getResource("AddRecordDialog.fxml"));
        } catch (IOException e) {
            System.out.println("Error in loading record dialog");
        }

        return dialog_root;
    }

    @FXML protected void handleAddRecordSubmitAction(ActionEvent evt) {

        System.out.println("Clicked on addButton");

        // Setup addRecordStage
        Parent addRecordRoot = loadDialogRoot();
        Scene addRecordScene = new Scene(addRecordRoot, 600, 400, Color.TRANSPARENT);

        StageManager.getStageManager().showScene(addRecordScene);
    }

    /*
     * Called after the constructor for this object. We assume nodes marked with @FXML are
     * already populated.
     */
    @FXML public void initialize() {
        recordTable.setItems(this.model.getRecords());

        selectColumn.setCellFactory(c -> new CheckBoxTableCell<>());
        selectColumn.setCellValueFactory(cell -> {
            return new ReadOnlyBooleanWrapper(false);
        });

        dateColumn.setCellValueFactory(cell -> {
            return new ReadOnlyObjectWrapper<LocalDate>(cell.getValue().getDate());
        });

        amountColumn.setCellValueFactory(cell -> {
            return new ReadOnlyObjectWrapper<String>(String.format("%.2f", cell.getValue().getAmount()));
        });

        reasonColumn.setCellValueFactory(cell -> {
            return new ReadOnlyObjectWrapper<String>(cell.getValue().getReason());
        });

        incomeSummary.textProperty().bind(this.model.incomeProperty().asString("%.2f"));
        outcomeSummary.textProperty().bind(this.model.outcomeProperty().asString("%.2f"));
        flowSummary.textProperty().bind(this.model.flowProperty().asString("%.2f"));
    }
    
}
