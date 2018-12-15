package ua.nure.kn.yavorovenko.usermanagement.gui;


import ua.nure.kn.yavorovenko.usermanagement.User;
import ua.nure.kn.yavorovenko.usermanagement.db.DatabaseException;
import ua.nure.kn.yavorovenko.usermanagement.util.Messages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class BrowsePanel extends JPanel implements ActionListener {

    private static final String ERROR_TITLE = Messages.getString("BrowsePanel.error_title");
    private static final int CONFIRM_DELETE_ACTION = 0;

    private MainFrame parent;

    private JScrollPane tablePanel;
    private JTable userTable;
    private JPanel buttonsPanel;

    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton detailsButton;

    public BrowsePanel(MainFrame mainFrame) {
        this.parent = mainFrame;
        initialize();
    }

    private void initialize() {
        this.setName("browsePanel");
        this.setLayout(new BorderLayout());
        this.add(getTablePanel(), BorderLayout.CENTER);
        this.add(getButtonsPanel(), BorderLayout.SOUTH);
    }

    private JPanel getButtonsPanel() {
        if (buttonsPanel == null) {
            buttonsPanel = new JPanel();
            buttonsPanel.add(getAddButton());
            buttonsPanel.add(getEditButton());
            buttonsPanel.add(getDeleteButton());
            buttonsPanel.add(getDetailsButton());
        }
        return buttonsPanel;
    }

    private JButton getAddButton() {
        if (addButton == null) {
            addButton = new JButton();
            addButton.setText(Messages.getString("BrowsePanel.add"));
            addButton.setName("addButton");
            addButton.setActionCommand("add");
            addButton.addActionListener(this);
        }
        return addButton;
    }

    private JButton getEditButton() {
        if (editButton == null) {
            editButton = new JButton();
            editButton.setText(Messages.getString("BrowsePanel.edit")); //localize
            editButton.setName("editButton");
            editButton.setActionCommand("edit");
            editButton.addActionListener(this);
        }
        return editButton;
    }


    private JButton getDeleteButton() {
        if (deleteButton == null) {
            deleteButton = new JButton();
            deleteButton.setText(Messages.getString("BrowsePanel.delete")); //localize
            deleteButton.setName("deleteButton");
            deleteButton.setActionCommand("delete");
            deleteButton.addActionListener(this);
        }
        return deleteButton;
    }

    private JButton getDetailsButton() {
        if (detailsButton == null) {
            detailsButton = new JButton();
            detailsButton.setText(Messages.getString("BrowsePanel.details")); //localize
            detailsButton.setName("detailsButton");
            detailsButton.setActionCommand("details");
            detailsButton.addActionListener(this);
        }
        return detailsButton;
    }

    private JScrollPane getTablePanel() {
        if (tablePanel == null) {
            tablePanel = new JScrollPane(getUserTable());
        }
        return tablePanel;
    }

    private JTable getUserTable() {
        if (userTable == null) {
            userTable = new JTable();
            userTable.setName("userTable");
        }
        return userTable;
    }

    public void initTable() {
        UserTableModel model;
        try {
            model = new UserTableModel(parent.getDao().findAll());
        } catch (DatabaseException e) {
            model = new UserTableModel(new ArrayList<>());
            JOptionPane.showMessageDialog(this, e.getMessage(), ERROR_TITLE,
                    JOptionPane.ERROR_MESSAGE);
        }
        getUserTable().setModel(model);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        if ("add".equalsIgnoreCase(actionCommand)) {
            this.setVisible(false);
            parent.showAddPanel();
        }

        if (userTable.getSelectedRow() != -1) {
            if ("details".equalsIgnoreCase(actionCommand)) {
                try {
                    User user = getSelectedUser();
                    this.setVisible(false);
                    parent.showDetailsPanel(user);
                } catch (DatabaseException e1) {
                    JOptionPane.showMessageDialog(this, e1.getMessage(), ERROR_TITLE,
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            if ("edit".equalsIgnoreCase(actionCommand)) {
                try {
                    User user = getSelectedUser();
                    this.setVisible(false);
                    parent.showEditPanel(user);
                } catch (DatabaseException e1) {
                    JOptionPane.showMessageDialog(this, e1.getMessage(), ERROR_TITLE,
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            if ("delete".equalsIgnoreCase(actionCommand)) {
                try {
                    User user = getSelectedUser();

                    int answer = JOptionPane.showConfirmDialog(this, Messages.getString("BrowsePanel.confirmDeleteAction") + user + "?");
                    if (answer == CONFIRM_DELETE_ACTION) {
                        parent.getDao().delete(user);
                        this.initTable();
                    }
                } catch (DatabaseException e1) {
                    JOptionPane.showMessageDialog(this, e1.getMessage(), ERROR_TITLE,
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private User getSelectedUser() throws DatabaseException {
        int row = userTable.getSelectedRow();
        long userId = Long.parseLong(String.valueOf(userTable.getValueAt(row, 0)));
        return parent.getDao().find(userId);
    }


}