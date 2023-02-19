package financetracker.gui.controller;

import financetracker.*;
import financetracker.Record;
import financetracker.gui.ControllerFactory;
import financetracker.gui.element.StylingDoubleTextTableCell;
import financetracker.gui.model.BalanceModel;
import financetracker.gui.ViewLoader;
import financetracker.gui.element.DatePickerTableCell;
import financetracker.gui.element.HighlightableTextFieldCell;
import financetracker.gui.element.IconTableCell;
import financetracker.gui.model.PeriodFilter;
import financetracker.gui.model.RecordTableModel;
import financetracker.io.Persistence;
import financetracker.io.export.CSV;
import financetracker.io.export.ColumnarExport;
import financetracker.io.export.Export;
import financetracker.io.export.OpenDocument;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

/**
 * The main controller. Ties to App.fxml.
 */
public class AppController implements ModelInjectable {

    private BalanceModel model;

    @FXML private TableView<RecordTableModel> recordTable;
    @FXML private ContextMenu tableContextMenu;
    @FXML private MenuItem deleteMenuItem;
    @FXML private TableColumn<RecordTableModel, LocalDate> dateColumn;
    @FXML private TableColumn<RecordTableModel, Double> amountColumn;
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
    @FXML private MenuItem columnarExportMenuItem;
    @FXML private MenuItem saveButton;
    private Persistence p;
    private NumberFormat fmt;
    private Stage searchWindow;
    private Scene searchScene;

    /**
     * Constructor. Sets up the persistence mechanism and a formatter for double parsing.
     */
    public AppController() {
        p = new Persistence();
        fmt = NumberFormat.getInstance(Locale.getDefault());
    }

    public void setModel(BalanceModel model) {
        this.model = model;
    }

    /**
     * Called after the constructor for this object. We assume nodes marked with @FXML are
     * already populated.
     * Binds the controls (nodes) to the model data. Whenever this data changes, the graphic is
     * automatically updated by the JFX render engine.
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

        dateColumn.setCellFactory(c -> new DatePickerTableCell<>());
        dateColumn.setCellValueFactory(cell -> cell.getValue().dateProperty());
        dateColumn.setOnEditCommit(event -> {
            event.getTableView().getSelectionModel().getSelectedItem().setDate(event.getNewValue());
        });

        amountColumn.setCellFactory(cell -> new StylingDoubleTextTableCell());
        amountColumn.setCellValueFactory(cell -> new SimpleObjectProperty<Double>(cell.getValue().getAmount()));
        amountColumn.setOnEditCommit(event -> {
            event.getTableView().getSelectionModel().getSelectedItem().setAmount(event.getNewValue());
        });

        reasonColumn.setCellFactory(cell -> new HighlightableTextFieldCell());
        reasonColumn.setCellValueFactory(cell -> cell.getValue().reasonProperty());
        reasonColumn.setOnEditCommit(e -> {
            e.getTableView().getSelectionModel().getSelectedItem().setReason(e.getNewValue());
        });

        recordTypeColumn.setCellValueFactory(cell -> cell.getValue().amountProperty().greaterThanOrEqualTo(0));
        recordTypeColumn.setCellFactory(c -> new IconTableCell<>());

        incomeSummary.textProperty().bind(this.model.incomeProperty().asString("%.2f"));
        outcomeSummary.textProperty().bind(this.model.outcomeProperty().asString("%.2f"));
        flowSummary.textProperty().bind(this.model.flowProperty().asString("%.2f"));

        opaqueLayer.visibleProperty().bind(addRecordDialog.visibleProperty());
    }

    @FXML protected void handleAddButtonClick() {
        addRecordDialog.setVisible(true);
        addRecordDialog.setManaged(true);
    }

    /**
     * Shows a dialog for selecting the custom separator when exporting
     * as a columnar file. Suggest the use of pipe '|' as separator
     * by default, but the user can enter whatever they want.
     * @return An optional value with separator.
     */
    private Optional<String> showCustomSeparatorSelector() {
        TextInputDialog d = new TextInputDialog("|");
        d.setTitle("Select the separator");
        d.setHeaderText("Select a separator for columnar export.");
        d.setContentText("Separator char:");

        return d.showAndWait();
    }

    /**
     * Shows a file dialog for data export.
     * @param extension the file extension.
     * @param exporter a data exporter, that implements {@link Export}.
     */
    private void showDialogAndExport(String extension, Export exporter) {
        FileChooser fChooser = new FileChooser();
        fChooser.setTitle("Export...");

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

    /**
     * Shows a file chooser to select the file for export. Calls the appropriate exporter
     * object to produce a file.
     * @param evt
     */
    @FXML protected void handleExport(ActionEvent evt) {
        String extension;
        Export exporter;

        if(evt.getSource() == csvExportMenuItem) {
            extension = ".csv";
            exporter = new CSV(this.model.getDao());

        } else if (evt.getSource() == openDocumentExportMenuItem) {
            extension = ".ods";
            exporter = new OpenDocument(this.model.getDao());

        } else if (evt.getSource() == columnarExportMenuItem) {
            extension = "";
            Optional<String> s = showCustomSeparatorSelector();

            if(s.isPresent())
                exporter = new ColumnarExport(this.model.getDao(), s.get());
            else
                return;

        } else {
            return;
        }

        showDialogAndExport(extension, exporter);
    }

    /**
     * Shows a file dialog to select the save location, and saves current data.
     */
    @FXML protected void handleSave() {
        FileChooser fChooser = new FileChooser();
        fChooser.setTitle("Save...");

        fChooser.setInitialFileName(p.getLastFile() != null ? p.getLastFile().getName() : "Untitled.dat");
        File file = fChooser.showSaveDialog(recordTable.getScene().getWindow());

        if(file != null) {
            try {
                p.saveData(this.model.getDao(), file);
                this.model.setEdited(false);
            } catch (IOException e) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Can't save to the specified location.", ButtonType.OK);
                a.showAndWait();
            }
        }
    }

    /**
     * Shows a file dialog to select the file to load from.
     */
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
                this.model.resetFilter();
            } catch (IOException e) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Can't load the specified file.", ButtonType.OK);
                a.showAndWait();
            }
        }

    }

    /**
     * Deletes a single selected row from the model. Callback for the right-click "Delete" action
     * on a row.
     */
    @FXML protected void handleSingleRowDelete() {
        RecordTableModel r = recordTable.getSelectionModel().getSelectedItem();

        if (r != null) {
            this.model.deleteRecord(r);
        }
    }

    /**
     * Deletes a set of rows, selected with the checkboxes on the first column.
     */
    @FXML protected void handleSelectedRowsDelete() {
        this.model.deleteRecords(
                recordTable.getItems().stream()
                        .filter(c -> c.isSelected())
        );
    }

    /**
     * Shows the custom period selector window.
     */
    @FXML protected void showCustomPeriodSelector() {
        Parent root = ViewLoader.load(
                "PeriodFilterSelector.fxml",
                cls -> ControllerFactory.buildController(cls, model)   //this lambda IS the factory method
        );

        Scene scene = new Scene(root, 300, 200);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Select custom period");
        stage.show();
    }

    /**
     * Checks if any checkbox is ticked, and shows the delete menu accordingly.
     */
    @FXML protected void handleEditMenuClick() {
        multirowDeleteMenu.setDisable(
            !recordTable.getItems().stream().map(r -> selectColumn.getCellData(r)).reduce(Boolean::logicalOr).orElse(false)
        );
    }

    /**
     * Builds the search scene, to populate the window.
     */
    private void loadSearchWindow() {
        Parent root = ViewLoader.load(
                "Search.fxml",
                cls -> ControllerFactory.buildController(cls, model, this)
        );
        searchScene = new Scene(root, 550, 155);
    }

    /**
     * Shows the search window. If it's the first time showing it, it creates the
     * window and the scene first.
     */
    @FXML protected void showSearch() {
        if(searchScene == null)
            loadSearchWindow();

        if(searchWindow == null) {
            searchWindow = new Stage();
            searchWindow.setResizable(false);
            searchWindow.setScene(searchScene);
            searchWindow.setTitle("Search");
            searchWindow.show();

            searchWindow.setOnCloseRequest(r -> highlightRecord(null));
        } else {
            searchWindow.show();
            searchWindow.requestFocus();
            searchWindow.toFront();
        }
    }

    static RecordTableModel highlightedRecord;

    /**
     * Highlights a row correspondent to the passed Record. If null is passed, clears all highlighting.
     * @param r the record, corresponding to a record row, to highlight.
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

    /**
     * Opens a dialog that asks the user if the works must be saved.
     */
    private void askToSave() {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "You have unsaved work. Do you want to save it?", ButtonType.NO, ButtonType.YES);
        if(a.showAndWait().get() == ButtonType.YES)
            handleSave();
    }

    /**
     * Resets the model when clicking on "File > Close"
     */
    public void handleClose() {
        if(this.model.isEdited())
            askToSave();

        this.model.resetAll();
    }

    /**
     * Deletes the current filter and shows all records.
     */
    @FXML protected void showAll() {
        this.model.filterRecords(PeriodFilter.ALL());
    }
}
