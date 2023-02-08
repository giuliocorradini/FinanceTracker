package financetracker.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class MainWindowController {

    @FXML private Button addButton;
    @FXML private ChoiceBox<String> periodSelector;

    private Parent loadDialogRoot() {
        Parent dialog_root = null;

        try {
            dialog_root = FXMLLoader.load(getClass().getClassLoader().getResource("AddRecordDialog.fxml"));
        } catch (IOException e) {
            System.out.println("Error in loading record dialog");
        }

        return dialog_root;
    }

    @FXML protected void handleAddRecordSubmitAction(ActionEvent evt) {

        System.out.println("Clicked on addButton");

        // Setup addRecordStage
        Parent addRecordRoot = loadDialogRoot();
        Scene addRecordScene = new Scene(addRecordRoot, 600, 400, Color.TRANSPARENT);

        StageManager.getStageManager().showScene(addRecordScene);
    }
    
}
