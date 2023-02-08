package financetracker;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;

import financetracker.gui.StageManager;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class App extends Application {

    public static void main(String[] args) {
        Balance bm = new Balance();

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Setup primaryStage
        primaryStage.setTitle("FinanceTracker");

        Parent root = null;

        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("App.fxml"));
        } catch (IOException e) {
            System.err.println(e);
            System.exit(-1);
        }


        Scene mainScene = new Scene(root, 600, 400);

        StageManager sm = StageManager.getStageManager();
        sm.setPrimaryStage(primaryStage);
        sm.showScene(mainScene);
    }
}
