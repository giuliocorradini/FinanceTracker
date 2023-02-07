package financetracker;

import java.io.IOError;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class App extends Application {

    public static void main(String[] args) {
        Balance bm = new Balance();

        // Add test
        bm.addRecord(100, "Deposito bancomat", LocalDate.of(2023, Month.JANUARY, 1));
        bm.addRecord(2000, "Stipendio", LocalDate.of(2023, Month.JANUARY, 28));
        bm.addRecord(-245, "Subscription office", LocalDate.of(2023, Month.FEBRUARY, 2));

        // Check data presence
        System.out.println("Hai inserito " + bm.getRecordCount() + " record");

        // Select a record
        Record query_res = bm.searchRecord("Deposito bancomat");

        // Edit selected record
        query_res.setReason("Deposito ATM");
        bm.editRecord(query_res);

        // Compute balance
        double total = bm.getRecordSum();
        System.out.println("Totale del bilancio: " + total);

        // Delete a record
        Record to_be_deleted = bm.searchRecord("Subscription office");
        if(to_be_deleted != null)
            bm.deleteRecord(to_be_deleted);

        // Compute balance pt.2
        total = bm.getRecordSum();
        System.out.println("Totale del bilancio: " + total);

        // Filter by date
        // get january only records
        System.out.println("Record di gennaio");
        LocalDate start_of_january = LocalDate.of(2023, Month.JANUARY, 01);
        LocalDate end_of_january = LocalDate.of(2023, Month.JANUARY, 31);
        Record[] filtered = BalanceFilter.getRecordsBetweenDates(bm, start_of_january, end_of_january);
        for(Record r: filtered) {
            System.out.println(r.getReason() + " " + r.getDate());
        }

        // Save data
        Persistence p = new Persistence();
        p.saveData(bm);

        // Load data
        Balance loaded_b = p.loadData();
        System.out.println("Il bilancio caricato ha " + loaded_b.getRecordCount() + " record");
        System.out.println("Totale del bilancio: " + loaded_b.getRecordSum());

        // Free text search
        BalanceSearchEngine se = new BalanceSearchEngine(bm);
        System.out.println("Risultato della ricerca:");
        for(Record r: se.search("banco").toList()) {
            System.out.println(r);
        }

        // Export to CSV
        Export csv_e = new CSV(bm, "financetracker.csv");
        try {
            csv_e.export();
        } catch (IOException e) {
            System.out.println(e);
        }
        //      Export to OpenDocument
        Export ods_e = new OpenDocument(bm, "financetracker.ods");
        try {
            ods_e.export();
        } catch (IOException e) {
            System.out.println(e);
        }

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("FinanceTracker");

        Parent root = null;

        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("App.fxml"));
        } catch (IOException e) {
            System.err.println(e);
            System.exit(-1);
        }

        Scene scene = new Scene(root, 300, 275);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
