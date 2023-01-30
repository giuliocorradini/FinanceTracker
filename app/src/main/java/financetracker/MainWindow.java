package financetracker;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class MainWindow extends JFrame {
    private JPanel container;
    private JTable currentRecords_table;
    private JButton addRecord_button;

    public MainWindow() {
        super();

        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));

        TableModel dt = new AbstractTableModel() {
            public int getColumnCount() {return 10;}
            public int getRowCount() {return 10;}
            public Object getValueAt(int row, int col) {return row*col;}
        };

        BalanceModel bdt = new BalanceModel();

        currentRecords_table = new JTable(bdt);

        addRecord_button = new JButton("Add");
        addRecord_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Adding record");
                bdt.addRecord();
            }
        });

        container.add(currentRecords_table);
        container.add(addRecord_button);

        this.add(container);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
