package ua.nure.kn.yavorovenko.usermanagement.gui;

import ua.nure.kn.yavorovenko.usermanagement.User;
import ua.nure.kn.yavorovenko.usermanagement.util.Messages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DetailsPanel extends JPanel implements ActionListener {
    private MainFrame parent;


    private JPanel buttonsPanel;
    private JPanel fieldPanel;

    private JButton closeButton;

    private JTextField fullNameField;
    private JTextField ageField;

    private User detailUser;

    public DetailsPanel(MainFrame parent) {
        this.parent = parent;
        initialize();
    }

    private void initialize() {
        this.setName("detailsPanel");
        this.setLayout(new BorderLayout());
        this.add(getFieldPanel(), BorderLayout.NORTH);
        this.add(getButtonsPanel(), BorderLayout.SOUTH);
    }

    private JPanel getFieldPanel() {
        if (fieldPanel == null) {
            fieldPanel = new JPanel();
            fieldPanel.setLayout(new GridLayout(2, 2));
            addLabeledField(fieldPanel, Messages.getString("DetailsPanel.full_name"), getFullNameField());
            addLabeledField(fieldPanel, Messages.getString("DetailsPanel.age"), getAgeField());
        }
        return fieldPanel;
    }

    private void addLabeledField(JPanel panel, String labelText, JTextField textField) {
        JLabel label = new JLabel(labelText);
        label.setLabelFor(textField);
        panel.add(label);
        panel.add(textField);
    }

    private JPanel getButtonsPanel() {
        if (buttonsPanel == null) {
            buttonsPanel = new JPanel();
            buttonsPanel.add(getCloseButton());
    }
        return buttonsPanel;
    }

    private JButton getCloseButton() {
        if (closeButton == null){
            closeButton = new JButton();
            closeButton.setText(Messages.getString("DetailsPanel.close"));
            closeButton.setName("closeButton");
            closeButton.addActionListener(this);
        }
        return closeButton;
    }

    private JTextField getFullNameField() {
        if (fullNameField == null) {
            fullNameField = new JTextField();
            fullNameField.setEditable(false);
            fullNameField.setName("fullNameField");
        }
        return fullNameField;
    }

    private JTextField getAgeField() {
        if (ageField == null) {
            ageField = new JTextField();
            ageField.setEditable(false);
            ageField.setName("ageField");
        }
        return ageField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.setVisible(false);
        parent.showBrowsePanel();
    }

    public void setUser(User detailUser) {
        this.detailUser = detailUser;
        getFullNameField().setText(this.detailUser.getFullName());
        getAgeField().setText(String.valueOf(this.detailUser.getAge()));
    }
}
