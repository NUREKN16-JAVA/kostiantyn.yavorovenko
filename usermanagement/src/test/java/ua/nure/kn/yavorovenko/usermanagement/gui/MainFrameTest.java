package ua.nure.kn.yavorovenko.usermanagement.gui;

import junit.extensions.jfcunit.JFCTestCase;
import junit.extensions.jfcunit.JFCTestHelper;
import junit.extensions.jfcunit.TestHelper;
import junit.extensions.jfcunit.eventdata.MouseEventData;
import junit.extensions.jfcunit.finder.NamedComponentFinder;
import ua.nure.kn.yavorovenko.usermanagement.util.Messages;

import javax.swing.*;
import java.awt.*;

public class MainFrameTest extends JFCTestCase {

    private MainFrame mainFrame;

    public void setUp() throws Exception {
        super.setUp();
        setHelper(new JFCTestHelper());
        mainFrame = new MainFrame();
        mainFrame.setVisible(true);
    }

    public void tearDown() throws Exception {
        super.tearDown();
        mainFrame.setVisible(false);
        TestHelper.cleanUp(this);
    }

    private Component find(Class<?> componentClass, String name){
        Component component;
        NamedComponentFinder finder = new NamedComponentFinder(componentClass, name);
        finder.setWait(0);
        component = finder.find(mainFrame, 0);
        assertNotNull("Could not find component '" + name + "'", component);
        return  component;
    }

    public void testBrowseControls() {
        find(JPanel.class, "browsePanel");
        JTable table = (JTable) find(JTable.class, "userTable");
        assertEquals("The table must contain 3 column!", 3, table.getColumnCount());

        assertEquals("The first column must be " + Messages.getResourceBundle().getString("UserTableModel.id"),
                Messages.getResourceBundle().getString("UserTableModel.id"),
                table.getColumnName(0));

        assertEquals("The second column must be " + Messages.getResourceBundle().getString("UserTableModel.first_name"),
                Messages.getResourceBundle().getString("UserTableModel.first_name"),
                table.getColumnName(1)); // localize

        assertEquals("The third column must be " + Messages.getResourceBundle().getString("UserTableModel.last_name"),
                Messages.getResourceBundle().getString("UserTableModel.last_name"),
                table.getColumnName(2)); // localize


        find(JButton.class, "addButton");
        find(JButton.class, "editButton");
        find(JButton.class, "deleteButton");
        find(JButton.class, "detailsButton");
    }

    public void testAddUser() {
        JButton addButton = (JButton) find(JButton.class, "addButton");
        getHelper().enterClickAndLeave(new MouseEventData(this, addButton));

        find(JPanel.class, "addPanel");
        find(JTextField.class, "firstNameField");
        find(JTextField.class, "lastNameField");
        find(JTextField.class, "dateOfBirthField");

        JButton okButton = (JButton) find(JButton.class, "okButton");
        find(JButton.class, "cancelButton");

        getHelper().enterClickAndLeave(new MouseEventData(this, okButton));
        find(JPanel.class, "browsePanel");
    }
}
