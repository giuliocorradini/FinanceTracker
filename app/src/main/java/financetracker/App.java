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

/**
 * This class provides the main entry point for the application.
 * It inherits from javafx.application.Application, and call the static method
 * launch of the superclass.
 * <p>
 * The {@link #start(Stage) start} method is responsible for preparing the environment
 * for the app to run: it creates the model, it loads the view from an FXML file and
 * connects everything with a suitable controller class.
 * </p>
 * <p>
 * Another duty of this class is to instantiate and provide an auto-save facility. This is
 * achieved by using a Timer, with the appropriate TimerTask, that spawns a thread in
 * background. Concurrency problem are handled with appropriate synchronized blocks
 * throughout the code.
 * </p>
 */
public class App extends Application {
    private BalanceModel model;
    private Timer t;
    private AutosaveTask autosaveFacility;

    /**
     * The constructor. Called somewhere by the superclass.
     * We don't instantiate directly, but rather call super.launch.
     */
    public App() {
        super();
        model = new BalanceModel(new Balance());
        t = new Timer();
        autosaveFacility = new AutosaveTask(this.model);
    }

    /**
     * This method is called by the superclass and provides the main window handle.
     * We load the application view using the {@link ViewLoader ViewLoader} class,
     * that takes an FXML name and a callback to build the controller.
     * Since we want to provide the controller a reference of our model, we use
     * a lambda function that encapsulates the call to {@link ControllerFactory#buildController(Class, BalanceModel) buildController}.
     *
     * <p>After the main window is loaded, the autosave facility is created and started.
     * If a temporary file is found, usually in the folder provided by "java.io.tmpdir",
     * a dialog is showed to the user that can choose to load it (or not).</p>
     *
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     */
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
                        this.model.setEdited(true);
                    } catch (IOException e) {
                        new Alert(Alert.AlertType.ERROR, "Can't restore from temporary file.", ButtonType.OK).showAndWait();
                    }
            });
        }

        t.scheduleAtFixedRate(autosaveFacility, 2*60*1000, 2*60*1000);

    }

    /**
     * This method is called when the main window exits. We perform cleanup
     * operations such as stopping the auto-save timer and deleting every temporary file
     * created.
     */
    @Override
    public void stop() {
        t.cancel();
        if(autosaveFacility != null)
            autosaveFacility.cleanup();
    }

    /**
     * The entry point for the whole application.
     * @param args command-line arguments, unused.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
