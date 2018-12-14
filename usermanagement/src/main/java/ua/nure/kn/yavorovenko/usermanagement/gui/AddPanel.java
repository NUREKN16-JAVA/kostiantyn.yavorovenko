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

public class AddPanel extends JPanel implements ActionListener {
    private static final String EMPTY_STRING = "";
    private static final Color EMPTY_BACKGROUND_COLOR = Color.WHITE;
    private static final String ERROR_TITLE = Messages.getString("AddPanel.error_title");
    private MainFrame parent;


    private JPanel buttonsPanel;
    private JPanel fieldPanel;

    private JButton okButton;
    private JButton cancelButton;

    private JTextField dateOfBirthField;
    private JTextField lastNameField;
    private JTextField firstNameField;

    public AddPanel(MainFrame parent) {
        this.parent = parent;
        initialize();
    }

    private void initialize() {
        this.setName("addPanel");
        this.setLayout(new BorderLayout());
        this.add(getFieldPanel(), BorderLayout.NORTH);
        this.add(getButtonsPanel(), BorderLayout.SOUTH);
    }

    private JPanel getFieldPanel() {
        if (fieldPanel == null) {
            fieldPanel = new JPanel();
            fieldPanel.setLayout(new GridLayout(3, 2));
            addLabeledField(fieldPanel, Messages.getString("AddPanel.first_name"), getFirstNameField());
            addLabeledField(fieldPanel, Messages.getString("AddPanel.last_name"), getLastNameField());
            addLabeledField(fieldPanel, Messages.getString("AddPanel.date_of_birth"), getDateOfBirthField());
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
        if (okButton == null){
            okButton = new JButton();
            okButton.setText(Messages.getString("AddPanel.ok"));
            okButton.setName("okButton");
            okButton.setActionCommand("ok");
            okButton.addActionListener(this);
        }
        return okButton;
    }

    private JButton getCancelButton() {
        if (cancelButton == null){
            cancelButton = new JButton();
            cancelButton.setText(Messages.getString("AddPanel.cancel"));
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
                User user = new User();
                user.setFirstName(getFirstNameField().getText());
                user.setLastName(getLastNameField().getText());
                DateFormat format = DateFormat.getDateInstance();
                user.setDateOfBirth(format.parse(getDateOfBirthField().getText()));
                parent.getDao().create(user);
            } catch (DatabaseException e1) {
                JOptionPane.showMessageDialog(this, e1.getMessage(), ERROR_TITLE,
                        JOptionPane.ERROR_MESSAGE);
            } catch (ParseException e1) {
                getDateOfBirthField().setBackground(Color.RED);
                return;
            }
        }
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
}
