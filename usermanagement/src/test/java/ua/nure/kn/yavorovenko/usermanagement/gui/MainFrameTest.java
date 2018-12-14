package ua.nure.kn.yavorovenko.usermanagement.gui;

import com.mockobjects.dynamic.Mock;
import junit.extensions.jfcunit.JFCTestCase;
import junit.extensions.jfcunit.JFCTestHelper;
import junit.extensions.jfcunit.TestHelper;
import junit.extensions.jfcunit.eventdata.MouseEventData;
import junit.extensions.jfcunit.eventdata.StringEventData;
import junit.extensions.jfcunit.finder.ComponentFinder;
import junit.extensions.jfcunit.finder.DialogFinder;
import junit.extensions.jfcunit.finder.Finder;
import junit.extensions.jfcunit.finder.NamedComponentFinder;
import ua.nure.kn.yavorovenko.usermanagement.User;
import ua.nure.kn.yavorovenko.usermanagement.db.DaoFactory;
import ua.nure.kn.yavorovenko.usermanagement.db.MockDaoFactory;
import ua.nure.kn.yavorovenko.usermanagement.util.Messages;

import javax.sql.rowset.JdbcRowSet;
import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class MainFrameTest extends JFCTestCase {

    private static final String FIRST_NAME_FOR_TUSER = "John";
    private static final String LAST_NAME_FOR_TUSER = "Doe";
    private static final Date DATE_OF_BIRTHDAY_FOR_TUSER = new Date();
    private static final String UPDATED_FIRST_NAME_FOR_TUSER = "Bob";

    private static final String DB_DAO_FACTORY = "dao.factory";

    private static final long ID_FOR_TUSER = 1L;
    private static final String EMPTY_STRING = "";
    private DateFormat dateFormat = DateFormat.getDateInstance();
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
        finder.setWait(10);
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
        User user = new User(
                FIRST_NAME_FOR_TUSER,
                LAST_NAME_FOR_TUSER,
                DATE_OF_BIRTHDAY_FOR_TUSER
        );
        User expectedUser = new User(
                ID_FOR_TUSER,
                FIRST_NAME_FOR_TUSER,
                LAST_NAME_FOR_TUSER,
                DATE_OF_BIRTHDAY_FOR_TUSER
        );

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

        getHelper().sendString(new StringEventData(this, firstNameField, FIRST_NAME_FOR_TUSER));

        getHelper().sendString(new StringEventData(this, lastNameField, LAST_NAME_FOR_TUSER));

        DateFormat formatter = DateFormat.getDateInstance();
        String date = formatter.format(DATE_OF_BIRTHDAY_FOR_TUSER);
        getHelper().sendString(new StringEventData(this, dateOfBirthField, date));

        getHelper().enterClickAndLeave(new MouseEventData(this, okButton));
        find(JPanel.class, "browsePanel");
        table = (JTable) find(JTable.class, "userTable");
        assertEquals(1, table.getRowCount());
    }

    public void testEditUser() {
        User initialUser = new User(
                ID_FOR_TUSER,
                FIRST_NAME_FOR_TUSER,
                LAST_NAME_FOR_TUSER,
                DATE_OF_BIRTHDAY_FOR_TUSER
        );
        User updatedUser = new User(
                ID_FOR_TUSER,
                UPDATED_FIRST_NAME_FOR_TUSER,
                LAST_NAME_FOR_TUSER,
                DATE_OF_BIRTHDAY_FOR_TUSER
        );

        ArrayList<User> userList = new ArrayList<>();
        userList.add(initialUser);

        mockUserDao.expectAndReturn("findAll", userList);
        mainFrame.showBrowsePanel();

        JTable table = (JTable) find(JTable.class, "userTable");

        table.setRowSelectionInterval(0,0);

        JButton editButton = (JButton) find(JButton.class, "editButton");
        mockUserDao.expectAndReturn("find",ID_FOR_TUSER, initialUser);
        getHelper().enterClickAndLeave(new MouseEventData(this, editButton));
        find(JPanel.class, "editPanel");

        JTextField firstNameField = (JTextField) find(JTextField.class, "firstNameField");
        JTextField lastNameField = (JTextField) find(JTextField.class, "lastNameField");
        JTextField dateOfBirthField = (JTextField) find(JTextField.class, "dateOfBirthField");

        assertEquals("The field must be filled", initialUser.getFirstName(), firstNameField.getText());
        assertEquals("The field must be filled", initialUser.getLastName(), lastNameField.getText());
        assertEquals("The field must be filled", dateFormat.format(initialUser.getDateOfBirth()), dateOfBirthField.getText());

        firstNameField.setText(EMPTY_STRING);
        getHelper().sendString(new StringEventData(this, firstNameField, UPDATED_FIRST_NAME_FOR_TUSER));

        JButton okButton = (JButton) find(JButton.class, "okButton");
        find(JButton.class, "cancelButton");

        mockUserDao.expectAndReturn("update", updatedUser, null);

        ArrayList<User> updatedUserList = new ArrayList<>();
        updatedUserList.add(updatedUser);
        mockUserDao.expectAndReturn("findAll", updatedUserList);
        getHelper().enterClickAndLeave(new MouseEventData(this, okButton));

        find(JPanel.class, "browsePanel");
        table = (JTable) find(JTable.class, "userTable");
        assertEquals(1, table.getRowCount());
    }

    public void testDeleteUser() {
        User deleteUser = new User(
                ID_FOR_TUSER,
                FIRST_NAME_FOR_TUSER,
                LAST_NAME_FOR_TUSER,
                DATE_OF_BIRTHDAY_FOR_TUSER
        );

        ArrayList<User> userList = new ArrayList<>();
        userList.add(deleteUser);

        mockUserDao.expectAndReturn("findAll", userList);
        mainFrame.showBrowsePanel();

        JTable table = (JTable) find(JTable.class, "userTable");

        table.setRowSelectionInterval(0,0);

        JButton deleteButton = (JButton) find(JButton.class, "deleteButton");
        mockUserDao.expectAndReturn("find",ID_FOR_TUSER, deleteUser);
        getHelper().enterClickAndLeave(new MouseEventData(this, deleteButton));

        List showingDialogs = TestHelper.getShowingDialogs();
        JDialog jDialog = (JDialog) showingDialogs.get(0);

        Finder buttonFinder = new ComponentFinder(JButton.class);
        JButton jb = (JButton)buttonFinder.find(jDialog, 0);

        mockUserDao.expectAndReturn("findAll", new ArrayList<>());
        mockUserDao.expectAndReturn("delete", deleteUser, null);
        getHelper().enterClickAndLeave(new MouseEventData(this, jb));

        find(JPanel.class, "browsePanel");
        table = (JTable) find(JTable.class, "userTable");
        assertEquals(0, table.getRowCount());
    }

    public void testDetailsUser() {
        User detailsUser = new User(
                ID_FOR_TUSER,
                FIRST_NAME_FOR_TUSER,
                LAST_NAME_FOR_TUSER,
                DATE_OF_BIRTHDAY_FOR_TUSER
        );

        ArrayList<User> userList = new ArrayList<>();
        userList.add(detailsUser);

        mockUserDao.expectAndReturn("findAll", userList);
        mainFrame.showBrowsePanel();

        JTable table = (JTable) find(JTable.class, "userTable");

        table.setRowSelectionInterval(0,0);

        JButton detailsButton = (JButton) find(JButton.class, "detailsButton");
        mockUserDao.expectAndReturn("find",ID_FOR_TUSER, detailsUser);
        getHelper().enterClickAndLeave(new MouseEventData(this, detailsButton));

        find(JPanel.class, "detailsPanel");
        
        JTextField fullNameField = (JTextField) find(JTextField.class, "fullNameField");
        JTextField ageField = (JTextField) find(JTextField.class, "ageField");

        assertEquals("The field must be filled", detailsUser.getFullName(), fullNameField.getText());
        assertEquals("The field must be filled", String.valueOf(detailsUser.getAge()), ageField.getText());


        JButton closeButton = (JButton) find(JButton.class, "closeButton");
        mockUserDao.expectAndReturn("findAll", userList);
        getHelper().enterClickAndLeave(new MouseEventData(this, closeButton));

        find(JPanel.class, "browsePanel");

    }
}
