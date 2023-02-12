package financetracker;

import financetracker.gui.BalanceModel;

public class ControllerFactory {

    /*
     * Generic method that instantiate a Controller class, and is called when a view is created.
     * Called by the custom FXML loader.
     * Each controller is tied to a view in its FXML file.
     * @param cont_class: the class to be instantiated
     * @return: a T object of the specified class
     */
    public static <T> T buildController(Class<T> cont_class, BalanceModel model) {
        //T cont = new T();
        //Can't directly instantiate objects like this.

        T cont = null;

        try {
            cont = cont_class.getDeclaredConstructor().newInstance();

            if (cont instanceof ModelInjectable) {
                ((ModelInjectable) cont).setModel(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cont;
    }
}
