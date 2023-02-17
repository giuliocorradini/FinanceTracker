module me.giuliocorradini.FinanceTracker {
    exports financetracker;
    exports financetracker.gui;
    exports financetracker.gui.controller;
    exports financetracker.gui.model;
    exports financetracker.gui.element;
    exports financetracker.io;
    exports financetracker.io.export;

    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires odfdom.java;
}