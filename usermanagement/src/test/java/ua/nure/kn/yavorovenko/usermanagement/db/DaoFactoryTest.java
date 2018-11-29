package ua.nure.kn.yavorovenko.usermanagement.db;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;


public class DaoFactoryTest{

    @Test
    public void getUserDaoTest() {
        try {
            DaoFactory daoFactory = DaoFactory.getInstance();
            assertNotNull("DaoFactory instance is null", daoFactory);

            UserDao userDao = daoFactory.getUserDao();
            assertNotNull("UserDao instance is null", userDao);
        } catch (NullPointerException e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
}