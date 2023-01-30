package financetracker;

import java.awt.event.*;
import javax.swing.*;

import financetracker.gui.AddRecordWindow;

public class MainWindow extends JFrame {
    private JPanel container;
    private JTable currentRecords_table;
    private JButton addRecord_button;
    private AddRecordWindow popup;

    public MainWindow(BalanceModel model) {
        super();

        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));

        GraphicalController controller = new MainWindowController(model);

        popup = new AddRecordWindow(this);

        currentRecords_table = new JTable(model);

        addRecord_button = new JButton("Add");
        addRecord_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Adding record");
                model.addRecord();
                popup.setVisible(true);
            }
        });

        container.add(currentRecords_table);
        container.add(addRecord_button);

        this.add(container);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
