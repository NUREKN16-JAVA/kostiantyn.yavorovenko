package ua.nure.kn.yavorovenko.usermanagement.gui;

import ua.nure.kn.yavorovenko.usermanagement.User;
import ua.nure.kn.yavorovenko.usermanagement.db.DaoFactory;
import ua.nure.kn.yavorovenko.usermanagement.db.UserDao;
import ua.nure.kn.yavorovenko.usermanagement.util.Messages;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public static final int FRAME_WIDTH = 800;
    public static final int FRAME_HEIGHT = 600;

    private JPanel contentPanel;
    private BrowsePanel browsePanel;
    private AddPanel addPanel;
    private UserDao dao;
    private EditPanel editPanel;
    private DetailsPanel detailsPanel;

    public MainFrame() throws HeadlessException {
        super();
        dao = DaoFactory.getInstance().getUserDao();
        initialize();
    }

    public UserDao getDao() {
        return dao;
    }

    private void initialize() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setTitle(Messages.getString("MainFrame.user_management"));
        this.setContentPane(getContentPanel());
    }

    private JPanel getContentPanel() {
        if (contentPanel == null) {
            contentPanel = new JPanel();
            contentPanel.setLayout(new BorderLayout());
            contentPanel.add(getBrowsePanel(), BorderLayout.CENTER);
        }
        return contentPanel;
    }

    private JPanel getBrowsePanel() {
        if (browsePanel == null) {
            browsePanel = new BrowsePanel(this);
        }
        browsePanel.initTable();
        return browsePanel;
    }

    public void showAddPanel() {
        showPanel(getAddPanel());
    }

    public void showBrowsePanel() {
        showPanel(getBrowsePanel());
    }

    public void showEditPanel(User editUser){
        showPanel(getEditPanel());
        editPanel.setUser(editUser);
    }

    public void showDetailsPanel(User detailUser) {
        showPanel(getDetailsPanel());
        detailsPanel.setUser(detailUser);
    }

    private DetailsPanel getDetailsPanel() {
        if (detailsPanel == null) {
            detailsPanel = new DetailsPanel(this);
        }
        return detailsPanel;
    }

    private AddPanel getAddPanel() {
        if (addPanel == null) {
            addPanel = new AddPanel(this);
        }
        return addPanel;
    }

    public EditPanel getEditPanel() {
        if (editPanel == null) {
            editPanel = new EditPanel(this);
        }
        return editPanel;
    }

    private void showPanel(JPanel panel) {
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setVisible(true);
        panel.repaint();
    }

    public static void main(String[] args) {
        new MainFrame().setVisible(true);
    }
}
