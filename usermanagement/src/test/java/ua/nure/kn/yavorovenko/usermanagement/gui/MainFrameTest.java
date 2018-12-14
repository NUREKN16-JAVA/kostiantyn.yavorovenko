package ua.nure.kn.yavorovenko.usermanagement.gui;

import com.mockobjects.dynamic.Mock;
import junit.extensions.jfcunit.JFCTestCase;
import junit.extensions.jfcunit.JFCTestHelper;
import junit.extensions.jfcunit.TestHelper;
import junit.extensions.jfcunit.eventdata.MouseEventData;
import junit.extensions.jfcunit.eventdata.StringEventData;
import junit.extensions.jfcunit.finder.NamedComponentFinder;
import ua.nure.kn.yavorovenko.usermanagement.User;
import ua.nure.kn.yavorovenko.usermanagement.db.DaoFactory;
import ua.nure.kn.yavorovenko.usermanagement.db.MockDaoFactory;
import ua.nure.kn.yavorovenko.usermanagement.util.Messages;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

public class MainFrameTest extends JFCTestCase {

    private static final String DB_DAO_FACTORY = "dao.factory";
    private MainFrame mainFrame;
    private Mock mockUserDao;

    public void setUp() throws Exception {
        super.setUp();

        Properties properties = new Properties();
        properties.setProperty(DB_DAO_FACTORY, MockDaoFactory.class.getName());
        DaoFactory.init(properties);

        mockUserDao = ((MockDaoFactory) DaoFactory.getInstance()).getMockUserDao();
        mockUserDao.expectAndReturn("findAll", new ArrayList());

        setHelper(new JFCTestHelper());
        mainFrame = new MainFrame();
        mainFrame.setVisible(true);
    }

    public void tearDown() throws Exception {
        try {
            mockUserDao.verify();
            mainFrame.setVisible(false);
            TestHelper.cleanUp(this);
            super.tearDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        assertEquals("The first column must be " + Messages.getString("UserTableModel.id"),
                Messages.getString("UserTableModel.id"),
                table.getColumnName(0));

        assertEquals("The second column must be " + Messages.getString("UserTableModel.first_name"),
                Messages.getString("UserTableModel.first_name"),
                table.getColumnName(1));

        assertEquals("The third column must be " + Messages.getString("UserTableModel.last_name"),
                Messages.getString("UserTableModel.last_name"),
                table.getColumnName(2));


        find(JButton.class, "addButton");
        find(JButton.class, "editButton");
        find(JButton.class, "deleteButton");
        find(JButton.class, "detailsButton");
    }


    public void testAddUser() {
        String firstName = "John";
        String lastName = "Doe";
        Date dateOfBirthday = new Date();

        User user = new User(firstName, lastName, dateOfBirthday);

        User expectedUser = new User(1L, firstName, lastName, dateOfBirthday);

        mockUserDao.expectAndReturn("create", user, expectedUser);

        ArrayList<User> userList = new ArrayList<>();
        userList.add(expectedUser);

        mockUserDao.expectAndReturn("findAll", userList);

        JTable table = (JTable) find(JTable.class, "userTable");
        assertEquals(0, table.getRowCount());

        JButton addButton = (JButton) find(JButton.class, "addButton");
        getHelper().enterClickAndLeave(new MouseEventData(this, addButton));

        find(JPanel.class, "addPanel");

        JTextField firstNameField = (JTextField) find(JTextField.class, "firstNameField");
        JTextField lastNameField = (JTextField) find(JTextField.class, "lastNameField");
        JTextField dateOfBirthField = (JTextField) find(JTextField.class, "dateOfBirthField");

        JButton okButton = (JButton) find(JButton.class, "okButton");
        find(JButton.class, "cancelButton");

        getHelper().sendString(new StringEventData(this, firstNameField, firstName));

        getHelper().sendString(new StringEventData(this, lastNameField, lastName));

        DateFormat formatter = DateFormat.getDateInstance();
        String date = formatter.format(dateOfBirthday);
        getHelper().sendString(new StringEventData(this, dateOfBirthField, date));

        getHelper().enterClickAndLeave(new MouseEventData(this, okButton));
        find(JPanel.class, "browsePanel");
        table = (JTable) find(JTable.class, "userTable");
        assertEquals(1, table.getRowCount());
    }
}
