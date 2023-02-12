package financetracker.gui;

import financetracker.*;
import financetracker.Record;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
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
    @FXML private MenuItem csvExportMenuItem;
    @FXML private MenuItem openDocumentExportMenuItem;

    public void setModel(BalanceModel model) {
        this.model = model;
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

    @FXML protected void handleAddButtonClick(ActionEvent evt) {
        addRecordDialog.setVisible(true);
        addRecordDialog.setManaged(true);
    }

    @FXML protected void handleExport(ActionEvent evt) {
        FileChooser fChooser = new FileChooser();
        fChooser.setTitle("Export...");

        String extension = "";
        Export exporter = null;

        if(evt.getSource() == csvExportMenuItem) {
            extension = ".csv";
            exporter = new CSV(this.model.getDao());
        } else if (evt.getSource() == openDocumentExportMenuItem) {
            extension = ".ods";
            exporter = new OpenDocument(this.model.getDao());
        } else {
            return;
        }

        fChooser.setInitialFileName("Untitled" + extension);
        File file = fChooser.showSaveDialog(recordTable.getScene().getWindow());

        try {
            exporter.export(file.getAbsolutePath());
        } catch (IOException e) {
            Alert a = new Alert(Alert.AlertType.ERROR, "Can't export to the specified location.", ButtonType.OK);
            a.showAndWait();
        }
    }

    @FXML protected void handleSave() {
        FileChooser fChooser = new FileChooser();
        fChooser.setTitle("Save...");

        fChooser.setInitialFileName("Untitled.dat");
        File file = fChooser.showSaveDialog(recordTable.getScene().getWindow());

        Persistence p = new Persistence(this.model.getDao());
        try {
            p.saveData(file.getAbsolutePath());
        } catch (IOException e) {
            Alert a = new Alert(Alert.AlertType.ERROR, "Can't save to the specified location.", ButtonType.OK);
            a.showAndWait();
        }
    }

    @FXML protected void handleLoad() {
        FileChooser fChooser = new FileChooser();
        fChooser.setTitle("Open data from file...");
        fChooser.setSelectedExtensionFilter(
                new FileChooser.ExtensionFilter("FinanceTracker data file", "*.dat")
        );

        File file = fChooser.showOpenDialog(recordTable.getScene().getWindow());

        if(file != null) {
            Persistence p = new Persistence();

            try {
                Balance b = p.loadData(file.getAbsolutePath());
                this.model.replaceDAO(b);
            } catch (IOException e) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Can't load the specified file.", ButtonType.OK);
                a.showAndWait();
            }
        }

    }

}
