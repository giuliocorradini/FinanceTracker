package financetracker.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Callback;

import java.io.IOException;

/**
 * This class provides a single static method that helps with FXML file instantiation.
 */
public class ViewLoader {
    /**
     * Load a view from an FXML file, and override the controller creation by providing a
     * custom factory method for controller linking. See {@link ControllerFactory}.
     * @param viewFilePath the FXML file path.
     * @param controllerFactory a callback for controller instantiation.
     * @return a loaded view, with nodes from the FXML file.
     */
    public static Parent load(String viewFilePath, Callback<Class<?>, Object> controllerFactory) {
        Parent parent = null;

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ViewLoader.class.getClassLoader().getResource(viewFilePath));
            loader.setControllerFactory(controllerFactory);

            parent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return parent;
    }
}
