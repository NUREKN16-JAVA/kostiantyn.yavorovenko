package ua.nure.kn.yavorovenko.usermanagement.gui;

import ua.nure.kn.yavorovenko.usermanagement.User;
import ua.nure.kn.yavorovenko.usermanagement.db.DatabaseException;
import ua.nure.kn.yavorovenko.usermanagement.util.Messages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;

public class EditPanel extends JPanel implements ActionListener {
    private static final String EMPTY_STRING = "";
    private static final Color EMPTY_BACKGROUND_COLOR = Color.WHITE;
    private static final String ERROR_TITLE = Messages.getString("EditPanel.error_title");

    private MainFrame parent;

    private JPanel buttonsPanel;
    private JPanel fieldPanel;

    private JButton okButton;
    private JButton cancelButton;

    private JTextField dateOfBirthField;
    private JTextField lastNameField;
    private JTextField firstNameField;

    private User updatedUser;

    public EditPanel(MainFrame parent) {
        this.parent = parent;
        initialize();
    }

    private void initialize() {
        this.setName("editPanel");
        this.setLayout(new BorderLayout());
        this.add(getFieldPanel(), BorderLayout.NORTH);
        this.add(getButtonsPanel(), BorderLayout.SOUTH);
    }

    private JPanel getFieldPanel() {
        if (fieldPanel == null) {
            fieldPanel = new JPanel();
            fieldPanel.setLayout(new GridLayout(3, 2));
            addLabeledField(fieldPanel, Messages.getString("EditPanel.first_name"), getFirstNameField());
            addLabeledField(fieldPanel, Messages.getString("EditPanel.last_name"), getLastNameField());
            addLabeledField(fieldPanel, Messages.getString("EditPanel.date_of_birth"), getDateOfBirthField());
        }
        return fieldPanel;
    }

    private JTextField getDateOfBirthField() {
        if (dateOfBirthField == null) {
            dateOfBirthField = new JTextField();
            dateOfBirthField.setName("dateOfBirthField");
        }
        return dateOfBirthField;
    }

    private JTextField getLastNameField() {
        if (lastNameField == null) {
            lastNameField = new JTextField();
            lastNameField.setName("lastNameField");
        }
        return lastNameField;
    }

    private JTextField getFirstNameField() {
        if (firstNameField == null) {
            firstNameField = new JTextField();
            firstNameField.setName("firstNameField");
        }
        return firstNameField;

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
            buttonsPanel.add(getOkButton());
            buttonsPanel.add(getCancelButton());
        }
        return buttonsPanel;
    }

    private JButton getOkButton() {
        if (okButton == null) {
            okButton = new JButton();
            okButton.setText(Messages.getString("EditPanel.ok"));
            okButton.setName("okButton");
            okButton.setActionCommand("ok");
            okButton.addActionListener(this);
        }
        return okButton;
    }

    private JButton getCancelButton() {
        if (cancelButton == null) {
            cancelButton = new JButton();
            cancelButton.setText(Messages.getString("EditPanel.cancel"));
            cancelButton.setName("cancelButton");
            cancelButton.setActionCommand("cancel");
            cancelButton.addActionListener(this);
        }
        return cancelButton;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("ok".equalsIgnoreCase(e.getActionCommand())) {
            try {
                updatedUser.setFirstName(getFirstNameField().getText());
                updatedUser.setLastName(getLastNameField().getText());
                DateFormat format = DateFormat.getDateInstance();
                updatedUser.setDateOfBirth(format.parse(getDateOfBirthField().getText()));
                parent.getDao().update(updatedUser);
            } catch (DatabaseException e1) {
                JOptionPane.showMessageDialog(this, e1.getMessage(), ERROR_TITLE,
                        JOptionPane.ERROR_MESSAGE);
            } catch (ParseException e1) {
                getDateOfBirthField().setBackground(Color.RED);
                return;
            }
        }
        updatedUser = null;
        clearFields();
        this.setVisible(false);
        parent.showBrowsePanel();
    }

    private void clearFields() {
        getFirstNameField().setText(EMPTY_STRING);
        getFirstNameField().setBackground(EMPTY_BACKGROUND_COLOR);

        getLastNameField().setText(EMPTY_STRING);
        getLastNameField().setBackground(EMPTY_BACKGROUND_COLOR);

        getDateOfBirthField().setText(EMPTY_STRING);
        getDateOfBirthField().setBackground(EMPTY_BACKGROUND_COLOR);
    }

    public void setUser(User editUser) {
        this.updatedUser = editUser;
        fillTextFields();
    }

    public void fillTextFields() {
        firstNameField.setText(updatedUser.getFirstName());
        lastNameField.setText(updatedUser.getLastName());
        DateFormat format = DateFormat.getDateInstance();
        dateOfBirthField.setText(format.format(updatedUser.getDateOfBirth()));
    }
}
