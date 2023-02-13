package financetracker.gui;

import financetracker.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;

public class AppController implements ModelInjectable {

    private BalanceModel model;

    @FXML private ChoiceBox<PeriodFilter> periodSelector;
    @FXML private TableView<RecordTableModel> recordTable;
    @FXML private ContextMenu tableContextMenu;
    @FXML private MenuItem deleteMenuItem;
    @FXML private TableColumn<RecordTableModel, LocalDate> dateColumn;
    @FXML private TableColumn<RecordTableModel, String> amountColumn;
    @FXML private TableColumn<RecordTableModel, String> reasonColumn;
    @FXML private TableColumn<RecordTableModel, Boolean> selectColumn;
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
        recordTable.setOnContextMenuRequested(evt -> {
            if(recordTable.getSelectionModel().isEmpty())
                deleteMenuItem.setDisable(true);
            else
                deleteMenuItem.setDisable(false);

            tableContextMenu.show(recordTable, evt.getScreenX(), evt.getScreenY());
        });
        tableContextMenu.setAutoHide(true);
        recordTable.addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> {
            if(evt.getButton() == MouseButton.PRIMARY && tableContextMenu.isShowing())
                tableContextMenu.hide();
        });

        recordTable.setEditable(true);

        selectColumn.setCellFactory(c -> new CheckBoxTableCell<>());
        selectColumn.setCellValueFactory(cell -> cell.getValue().selectedProperty());

        dateColumn.setCellValueFactory(cell -> cell.getValue().dateProperty());

        amountColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(String.format("%.2f", cell.getValue().getAmount())));

        reasonColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getReason()));

        incomeSummary.textProperty().bind(this.model.incomeProperty().asString("%.2f"));
        outcomeSummary.textProperty().bind(this.model.outcomeProperty().asString("%.2f"));
        flowSummary.textProperty().bind(this.model.flowProperty().asString("%.2f"));

        periodSelector.setItems(FXCollections.observableArrayList(PeriodFilter.values()));
    }

    @FXML protected void handleAddButtonClick() {
        addRecordDialog.setVisible(true);
        addRecordDialog.setManaged(true);
    }

    @FXML protected void handleExport(ActionEvent evt) {
        FileChooser fChooser = new FileChooser();
        fChooser.setTitle("Export...");

        String extension;
        Export exporter;

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
            try {
                Balance b = Persistence.loadData(file.getAbsolutePath());
                this.model.replaceDAO(b);
                periodSelector.setValue(PeriodFilter.ALL);
                this.model.resetFilter();
            } catch (IOException e) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Can't load the specified file.", ButtonType.OK);
                a.showAndWait();
            }
        }

    }

    @FXML protected void handleSingleRowDelete() {
        RecordTableModel r = recordTable.getSelectionModel().getSelectedItem();

        if (r != null) {
            this.model.deleteRecord(r);
        }
    }

    @FXML protected void handleSelectedRowsDelete() {
        this.model.deleteRecords(
                recordTable.getItems().stream()
                        .filter(c -> c.isSelected())
        );
    }

    @FXML protected void handleFilterSelection() {
        PeriodFilter f = periodSelector.getSelectionModel().getSelectedItem();
        if(f != null)
            this.model.filterRecords(f);
    }

}
