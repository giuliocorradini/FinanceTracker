package financetracker.gui.controller;

import financetracker.gui.model.BalanceModel;

/**
 * This interface is used by controllers that need a BalanceModel instance.
 */
public interface ModelInjectable {
    void setModel(BalanceModel b);
}
