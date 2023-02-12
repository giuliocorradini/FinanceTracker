package financetracker.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Callback;

import java.io.IOException;

public class ViewLoader {
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
