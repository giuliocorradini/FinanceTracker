package financetracker.gui;

import financetracker.Balance;
import financetracker.ControllerFactory;
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
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
    @FXML private Pane addRecordDialog;

    public void setModel(BalanceModel model) {
        this.model = model;
    }

    @FXML protected void handleAddButtonClick(ActionEvent evt) {
        addRecordDialog.setVisible(true);
        addRecordDialog.setManaged(true);
    }

    /*
     * Called after the constructor for this object. We assume nodes marked with @FXML are
     * already populated.
     */
    @FXML public void initialize() {
        addRecordDialog.setVisible(false);
        addRecordDialog.setManaged(false);

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
