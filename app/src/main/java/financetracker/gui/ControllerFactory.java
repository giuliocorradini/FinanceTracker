package financetracker.gui;

import financetracker.gui.controller.AppController;
import financetracker.gui.controller.ControllerInjectable;
import financetracker.gui.controller.ModelInjectable;
import financetracker.gui.model.BalanceModel;

/**
 * Factory of controllers. Provides a way of instatiating a controller and inject the application
 * model or the main controller (an instance of {@link AppController} to achieve the MVC pattern.
 *
 * <p>
 *     A model is injected if the specified controller class implements the {@link ModelInjectable}
 *     interface.
 *     The main AppController is injected if the class implements {@link ControllerInjectable}.
 *     This is used by secondary windows that want to send an event to the main window.
 *     See {@link financetracker.gui.controller.SearchController} for a use example.
 * </p>
 */
public class ControllerFactory {

    /**
     * Generic method that instantiate a Controller class, and is called when a view is created.
     * Called by the custom FXML loader.
     * Each controller is tied to a view in its FXML file.
     * @param cont_class the class to be instantiated
     * @return a T object of the specified class
     */
    public static <T> T buildController(Class<T> cont_class, BalanceModel model, AppController app) {
        //T cont = new T();
        //Can't directly instantiate objects like this.

        T cont = null;

        try {
            cont = cont_class.getDeclaredConstructor().newInstance();

            if (cont instanceof ModelInjectable && model != null) {
                ((ModelInjectable) cont).setModel(model);
            }

            if (cont instanceof ControllerInjectable && app != null) {
                ((ControllerInjectable) cont).setController(app);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cont;
    }

    /**
     * See {@link #buildController(Class, BalanceModel, AppController)}.
     */
    public static <T> T buildController(Class<T> cont_class, BalanceModel model) {
        return buildController(cont_class, model, null);
    }
}
