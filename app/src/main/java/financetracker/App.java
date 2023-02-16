package financetracker;

import financetracker.gui.ControllerFactory;
import financetracker.gui.model.BalanceModel;
import financetracker.gui.ViewLoader;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Timer;

public class App extends Application {
    private BalanceModel model;
    private Timer t;
    private AutosaveTask autosaveFacility;

    public App() {
        super();
        model = new BalanceModel(new Balance());
        t = new Timer();
        autosaveFacility = new AutosaveTask(this.model);
    }

    @Override
    public void start(Stage primaryStage) {
        Parent root = ViewLoader.load(
                "App.fxml",
                cls -> ControllerFactory.buildController(cls, model)   //this lambda IS the factory method
        );

        Scene scene = new Scene(root, 1200, 800);

        primaryStage.setScene(scene);
        primaryStage.setTitle("FinanceTracker");
        primaryStage.setMinHeight(500);
        primaryStage.setMinWidth(600);
        primaryStage.show();


        if (autosaveFacility.checkForTempPresence()) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "A temporary save file has been found, do you want to restore it?", ButtonType.YES, ButtonType.NO);
            a.showAndWait().ifPresent(resp -> {
                if (resp == ButtonType.YES)
                    try {
                        this.model.replaceDAO(autosaveFacility.loadFromTemp());
                    } catch (IOException e) {
                        new Alert(Alert.AlertType.ERROR, "Can't restore from temporary file.", ButtonType.OK).showAndWait();
                    }
            });
        }

        t.scheduleAtFixedRate(autosaveFacility, 2*60*1000, 2*60*1000);

    }

    @Override
    public void stop() {
        t.cancel();
        if(autosaveFacility != null)
            autosaveFacility.cleanup();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
