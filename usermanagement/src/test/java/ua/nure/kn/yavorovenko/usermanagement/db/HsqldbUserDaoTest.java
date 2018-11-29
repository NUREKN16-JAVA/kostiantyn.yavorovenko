package ua.nure.kn.yavorovenko.usermanagement.db;

import org.dbunit.DatabaseTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.nure.kn.yavorovenko.usermanagement.User;

import java.text.SimpleDateFormat;
import java.util.Collection;

public class HsqldbUserDaoTest extends DatabaseTestCase {

    private DaoFactory daoFactory = DaoFactory.getInstance();

    @Override
    protected IDatabaseConnection getConnection() throws Exception {
        return new DatabaseConnection(daoFactory.getConnectionFactory().createConnection());
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        return new XmlDataSet(
                getClass().getClassLoader().getResourceAsStream("usersDataSet.xml")
        );
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testCreate() throws Exception {
        User user  = new User(
                null,
                "Jack",
                "Nickolsan",
                new SimpleDateFormat("dd/MM/yyyy").parse("11/02/1992")
        );
        assertNull(user.getId());
        try {
            user = daoFactory.getUserDao().create(user);
        } catch (DatabaseException e) {
            e.printStackTrace();
            fail(e.toString());
        }
        assertNotNull("User is null!", user);
        assertNotNull("Field ID in user is null!", user.getId());
    }

    @Test
    public void testFindById() throws Exception {
        User user = null;
        try {
            user = daoFactory.getUserDao().find(1000L);
        } catch (DatabaseException e) {
            e.printStackTrace();
            fail(e.toString());
        }

        assertNotNull(user);
        assertEquals("The first name must be <Bill>", "Bill", user.getFirstName());
        assertEquals("The first name must be <Gates>", "Gates", user.getLastName());
        assertEquals("The date must be <1968-04-26>",
                new SimpleDateFormat("yyyy-MM-dd").parse("1968-04-26"),
                user.getDateOfBirth()
        );
    }

    @Test
    public void testFindAll() {
        Collection<User> users = null;
        try {
            users = daoFactory.getUserDao().findAll();
        } catch (DatabaseException e) {
            e.printStackTrace();
            fail(e.toString());
        }
        assertNotNull("Collection is null", users);
        assertEquals("Collection size.",2, users.size());
    }

    @Test
    public void testUpdate() throws Exception {
        User user  = new User(
                null,
                "Bob",
                "Lolybomb",
                new SimpleDateFormat("dd/MM/yyyy").parse("27/10/1983")
        );
        try {
            user = daoFactory.getUserDao().create(user);

            user.setFirstName("Nick");
            user.setLastName("Jonson");

            daoFactory.getUserDao().update(user);

            User updatedUser = daoFactory.getUserDao().find(user.getId());

            assertEquals("The first name must be <Nick>", "Nick", updatedUser.getFirstName());
            assertEquals("The first name must be <Jonson>", "Jonson", updatedUser.getLastName());
        } catch (DatabaseException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    @Test
    public void testDelete() throws Exception {
        User user = new User(
                1001L,
                "George",
                "Bush",
                new SimpleDateFormat("yyyy-MM-dd").parse("1968-04-26")
        );

        int countOfUsers = -1;
        try {
            daoFactory.getUserDao().delete(user);
            countOfUsers = daoFactory.getUserDao().findAll().size();
        } catch (DatabaseException e) {
            e.printStackTrace();
            fail(e.toString());
        }

        try {
            daoFactory.getUserDao().find(user.getId());
            fail("The find(Long id) method must throws Exception");
        } catch (DatabaseException e){
            //nop
        }
        assertEquals("After delete const user<1001L, George, Bush>, the count of users must be 1",1, countOfUsers);
    }
}