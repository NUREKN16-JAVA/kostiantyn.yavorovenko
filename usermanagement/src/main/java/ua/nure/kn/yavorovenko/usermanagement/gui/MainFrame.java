package ua.nure.kn.yavorovenko.usermanagement.gui;

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
        this.setTitle(Messages.getResourceBundle().getString("MainFrame.user_management"));
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

    private AddPanel getAddPanel() {
        if (addPanel == null) {
            addPanel = new AddPanel(this);
        }
        return addPanel;
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
