package ua.nure.kn.yavorovenko.usermanagement.web;

import ua.nure.kn.yavorovenko.usermanagement.User;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class BrowseServletTest extends MockServletTestCase {

    private static final Long ID_FOR_TUSER = 1000L;
    private static final String FIRST_NAME_FOR_TUSER = "John";
    private static final String LAST_NAME_FOR_TUSER = "Doe";
    private static final Date DATE_OF_BIRTHDAY_FOR_TUSER = new Date();

    @Override
    public void setUp() throws Exception {
        super.setUp();
        createServlet(BrowseServlet.class);
    }

    public void testBrowse() {
        User user = new User(
                ID_FOR_TUSER,
                FIRST_NAME_FOR_TUSER,
                LAST_NAME_FOR_TUSER,
                DATE_OF_BIRTHDAY_FOR_TUSER
        );

        List<User> listOfUsers = Collections.singletonList(user);

        getMockUserDao().expectAndReturn("findAll", listOfUsers);

        doGet();

        Collection<User> collectionOfUsers = (Collection<User>) getWebMockObjectFactory()
                .getMockSession()
                .getAttribute("users");
        assertNotNull("Could not find list of users in sessions", collectionOfUsers);
        assertSame(listOfUsers, collectionOfUsers);

    }
}
