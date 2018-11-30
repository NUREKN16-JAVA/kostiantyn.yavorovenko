package ua.nure.kn.yavorovenko.usermanagement.gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public static final int FRAME_WIDTH = 800;
    public static final int FRAME_HEIGHT = 600;
    private JPanel contentPanel;
    private BrowsePanel browsePanel;

    public MainFrame() throws HeadlessException {
        super();
        initialize();
    }

    private void initialize() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setTitle("User management"); //localize
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
        return browsePanel;
    }

    public static void main(String[] args) {

    }
}
