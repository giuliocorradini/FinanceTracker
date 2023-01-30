package financetracker.gui;

import java.awt.*;
import javax.swing.*;

public class AddRecordWindow extends JDialog {


    public AddRecordWindow(JFrame parent) {
        super(parent, "Add new record");

        Container contentPane = getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        JLabel amountLabel = new JLabel("Amount");
        JTextField amountField = new JTextField();
        
        JLabel reasonLabel = new JLabel("Reason");
        JTextField reasonField = new JTextField();
        
        JLabel datetimeLabel = new JLabel("Datetime");
        JTextField datetimeField = new JTextField();
        
        contentPane.add(amountLabel);
        contentPane.add(amountField);
        contentPane.add(reasonLabel);
        contentPane.add(reasonField);
        contentPane.add(datetimeLabel);
        contentPane.add(datetimeField);

        layout.putConstraint(SpringLayout.WEST, datetimeLabel, 5, SpringLayout.WEST, datetimeField);

        pack();
    }
}
