package financetracker.gui;

import java.awt.*;
import javax.swing.*;

public class AddRecordWindow extends JDialog {
    private static final int FIELDS = 3;

    public AddRecordWindow(JFrame parent) {
        super(parent, "Add new record");

        Container contentPane = getContentPane();

        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);


        // Create label:textfield pairs

        JLabel labels[] = {
            new JLabel("Amount"),
            new JLabel("Reason"),
            new JLabel("Datetime")
        };
        
        JTextField textFields[] = new JTextField[FIELDS];

        for(int i=0; i<FIELDS; i++) {
            textFields[i] = new JTextField(20);
            labels[i].setLabelFor(textFields[i]);
        }


        // Add components to contentPane and position
        for(int i=0; i<FIELDS; i++) {
            JLabel label = labels[i];
            JTextField textfield = textFields[i];

            contentPane.add(label);
            contentPane.add(textfield);

            layout.putConstraint(SpringLayout.WEST, label, 10, SpringLayout.WEST, contentPane);
            layout.putConstraint(SpringLayout.WEST, textfield, 20, SpringLayout.EAST, label);
            layout.putConstraint(SpringLayout.BASELINE, label, 0, SpringLayout.BASELINE, textfield);
            layout.putConstraint(SpringLayout.EAST, textfield, -10, SpringLayout.EAST, contentPane);

            if(i > 0) {
                layout.putConstraint(SpringLayout.NORTH, label, 10, SpringLayout.SOUTH, labels[i-1]);
                layout.putConstraint(SpringLayout.NORTH, textfield, 10, SpringLayout.SOUTH, textFields[i-1]);
                layout.putConstraint(SpringLayout.WEST, textfield, 0, SpringLayout.WEST, textFields[i-1]);
            } else if (i == 0) {
                layout.putConstraint(SpringLayout.NORTH, textfield, 10, SpringLayout.NORTH, contentPane);
            }
        }

        layout.putConstraint(SpringLayout.SOUTH, textFields[FIELDS-1], Spring.constant(-10), SpringLayout.SOUTH, contentPane);

        pack();
        
        setVisible(true);
    }
}
