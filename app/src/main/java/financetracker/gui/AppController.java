package financetracker.gui;

import financetracker.*;
import financetracker.Record;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Locale;

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
    @FXML private TableColumn<RecordTableModel, Boolean> recordTypeColumn;
    @FXML private MenuItem multirowDeleteMenu;
    @FXML private Pane opaqueLayer;
    private Persistence p;
    private NumberFormat fmt;

    public AppController() {
        fmt = NumberFormat.getInstance(Locale.getDefault());
    }

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
        dateColumn.setCellFactory(c -> new DatePickerTableCell<>());
        dateColumn.setOnEditCommit(event -> {
            event.getTableView().getSelectionModel().getSelectedItem().setDate(event.getNewValue());
        });

        amountColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        amountColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(String.format("%.2f", cell.getValue().getAmount())));
        amountColumn.setOnEditCommit(event -> { //invoked by TableCell.updateItem
            RecordTableModel selectedItem = event.getTableView().getSelectionModel().getSelectedItem();
            try {
                Double newval = fmt.parse(event.getNewValue()).doubleValue();
                selectedItem.setAmount(newval);
            } catch (ParseException e) {
                //TODO: red-bordered cell
            }
        });

        reasonColumn.setCellFactory(cell -> new HighlightableTextFieldCell());
        reasonColumn.setCellValueFactory(cell -> cell.getValue().reasonProperty());
        reasonColumn.setOnEditCommit(e -> {
            e.getTableView().getSelectionModel().getSelectedItem().setReason(e.getNewValue());
        });

        recordTypeColumn.setCellValueFactory(cell -> new ReadOnlyBooleanWrapper(cell.getValue().getAmount() >= 0));
        recordTypeColumn.setCellFactory(c -> new IconTableCell<>());

        incomeSummary.textProperty().bind(this.model.incomeProperty().asString("%.2f"));
        outcomeSummary.textProperty().bind(this.model.outcomeProperty().asString("%.2f"));
        flowSummary.textProperty().bind(this.model.flowProperty().asString("%.2f"));

        periodSelector.setItems(FXCollections.observableArrayList(PeriodFilter.values()));
        periodSelector.setValue(PeriodFilter.ALL);
        this.model.periodFilterProperty().addListener(
                (obs, ov, nv) -> periodSelector.setValue(nv)
        );

        opaqueLayer.visibleProperty().bind(addRecordDialog.visibleProperty());

        // This must be done here, instead of the constructor because the object model must be populated
        // and this is done by ControllerFactory after calling .newInstance on this class.
        p = new Persistence(this.model.getDao());
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

        if(file != null) {
            try {
                exporter.export(file.getAbsolutePath());
            } catch (IOException e) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Can't export to the specified location.", ButtonType.OK);
                a.showAndWait();
            }
        }
    }

    @FXML protected void handleSave() {
        FileChooser fChooser = new FileChooser();
        fChooser.setTitle("Save...");

        fChooser.setInitialFileName(p.getLastFile() != null ? p.getLastFile().getName() : "Untitled.dat");
        File file = fChooser.showSaveDialog(recordTable.getScene().getWindow());

        if(file != null) {
            try {
                p.saveData(file);
            } catch (IOException e) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Can't save to the specified location.", ButtonType.OK);
                a.showAndWait();
            }
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
                Balance b = p.loadData(file);
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

    private void showCustomPeriodSelector() {
        Parent root = ViewLoader.load(
                "CustomPeriodFilterSelector.fxml",
                cls -> ControllerFactory.buildController(cls, model)   //this lambda IS the factory method
        );

        Scene scene = new Scene(root, 300, 200);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Select custom period");
        stage.show();
    }

    @FXML protected void handleFilterSelection(ActionEvent evt) {
        PeriodFilter f = periodSelector.getValue();
        if(f != null) {
            if(f == PeriodFilter.CUSTOM) {
                showCustomPeriodSelector();
            }

            this.model.filterRecords(f);
        }
    }

    @FXML protected void handleEditMenuClick() {
        multirowDeleteMenu.setDisable(
            !recordTable.getItems().stream().map(r -> selectColumn.getCellData(r)).reduce(Boolean::logicalOr).orElse(false)
        );
    }

    public void handlePrint() {
        PrinterJob p = PrinterJob.createPrinterJob();
        if(p != null) {
            boolean print = p.showPrintDialog(recordTable.getScene().getWindow());
            if(print) {
                boolean success = p.printPage(recordTable);
                if(success) p.endJob();
            }
        }
    }

    @FXML protected void showSearch() {
        Parent root = ViewLoader.load(
                "Search.fxml",
                cls -> ControllerFactory.buildController(cls, model, this)
        );

        Scene scene = new Scene(root, 550, 155);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Search");
        stage.show();

        stage.setOnCloseRequest(r -> highlightRecord(null));
    }

    static RecordTableModel highlightedRecord;

    /*
     * Highlights a row correspondent to the passed Record. If null is passed, clears all highlighting.
     */
    public void highlightRecord(Record r) {
        // Reset previous highlighted, if present
        if(highlightedRecord != null)
            highlightedRecord.setHighlighted(false);

        // Get row from Record
        if(r != null) {
            highlightedRecord = recordTable.getItems().stream().filter(m -> m.getRecord() == r).findAny().orElse(null);

            // Highlight new
            if (highlightedRecord != null) {
                highlightedRecord.setHighlighted(true);
            }
        }

        recordTable.refresh();
    }
}
