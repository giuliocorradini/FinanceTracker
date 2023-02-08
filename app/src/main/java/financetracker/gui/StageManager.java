package financetracker.gui;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Stack;

public class StageManager {
    private static StageManager singleton = null;

    private Stack<Scene> scenes;

    private Stage primaryStage;

    private StageManager() {
        this.scenes = new Stack<Scene>();
    }

    public static StageManager getStageManager() {
        if(singleton == null) {
            singleton = new StageManager();
        }

        return singleton;
    }

    public static void SwitchSceneInEvent(Scene s, Event e) {
        Stage eventStage = (Stage)((Node)e.getSource()).getScene().getWindow();

        eventStage.setScene(s);
        eventStage.show();
    }

    public void showScene(Scene s) {
        this.scenes.push(s);

        this.primaryStage.setScene(s);
        this.primaryStage.show();
    }

    public void restorePreviousScene() {
        this.scenes.pop();

        this.primaryStage.setScene(this.scenes.peek());
        this.primaryStage.show();
    }

    public void setPrimaryStage(Stage s) {
        this.primaryStage = s;
    }
}
