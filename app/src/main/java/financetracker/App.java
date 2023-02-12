package financetracker;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;

import financetracker.gui.BalanceModel;
import financetracker.gui.StageManager;
import financetracker.gui.ViewLoader;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class App extends Application {
    BalanceModel model;

    public App() {
        super();
    }

    @Override
    public void init() {
        Balance b = new Balance();
        model = new BalanceModel(b);

    }

    @Override
    public void start(Stage primaryStage) {
        Parent root = ViewLoader.load(
                "App.fxml",
                cls -> { return ControllerFactory.buildController(cls, model); }   //this lambda IS the factory method
        );

        Scene scene = new Scene(root, 600, 400);

        primaryStage.setScene(scene);
        primaryStage.setTitle("FinanceTracker");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
