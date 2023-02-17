package financetracker.gui.controller;

import financetracker.gui.controller.AppController;

/**
 * This is interface is used by controllers that need a reference to an AppController.
 */
public interface ControllerInjectable {
    public void setController(AppController c);
}
