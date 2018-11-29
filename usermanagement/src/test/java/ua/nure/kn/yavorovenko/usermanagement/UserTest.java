package ua.nure.kn.yavorovenko.usermanagement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * Tests were written based on the date(UTC 22.11.2018);
 */
public class UserTest {
    private static final String FIRST_NAME = "Ivan";
    private static final String LAST_NAME = "Ivanov";
    private static final String FULL_NAME_ETALONE = "Ivanov, Ivan";

    private static final int ETALONE_AGE_1 = 47;
    private static final int DAY_OF_BIRTH_1 = 23;
    private static final int MONTH_OF_BIRTH_1 = Calendar.NOVEMBER;
    private static final int YEAR_OF_BIRTH_1 = 1971;

    private static final int YEAR_OF_BIRTH_2 = 2000;
    private static final int MONTH_OF_BIRTH_2 = Calendar.NOVEMBER;
    private static final int DATE_OF_BIRTH_2 = 21;
    private static final int ETALONE_AGE_2 = 18;

    private static final int YEAR_OF_BIRTH_3 = 2008;
    private static final int MONTH_OF_BIRTH_3 = Calendar.FEBRUARY;
    private static final int DATE_OF_BIRTH_3 = 29;
    private static final int ETALONE_AGE_3 = 10;

    private static final int YEAR__OF_BIRTH_4 = 1994;
    private static final int ETALONE_AGE_4 = 24;

    private User user;

    /**
     * Test 1 for the case when the birthday has already passed,
     * but the month is still this year
     *
     * @version 22.11.2018
     */
    @Test
    public void testGetAge1() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(YEAR_OF_BIRTH_1, MONTH_OF_BIRTH_1, DAY_OF_BIRTH_1);
        user.setDateOfBirth(calendar.getTime());

        assertEquals(ETALONE_AGE_1, user.getAge());
    }

    /**
     * Test 2 for the case when the birthday is before today,
     * but the month is still this year
     *
     * @version 22.11.2018
     */
    @Test
    public void testGetAge2() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(YEAR_OF_BIRTH_2, MONTH_OF_BIRTH_2, DATE_OF_BIRTH_2);
        user.setDateOfBirth(calendar.getTime());

        assertEquals(ETALONE_AGE_2, user.getAge());
    }

    /**
     * Test 3 for the case when the birthday has been in leap year
     *
     * @version 22.11.2018
     */
    @Test
    public void testGetAge3() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(YEAR_OF_BIRTH_3, MONTH_OF_BIRTH_3, DATE_OF_BIRTH_3);
        user.setDateOfBirth(calendar.getTime());

        assertEquals(ETALONE_AGE_3, user.getAge());
    }

    /**
     * Test 4 for the case when the birthday is today
     *
     * @version 22.11.2018
     */
    @Test
    public void testGetAge4() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, YEAR__OF_BIRTH_4);
        user.setDateOfBirth(calendar.getTime());

        assertEquals(ETALONE_AGE_4, user.getAge());
    }

    /**
     * Test for for the case when the birthday is set to a future date
     *
     * @version 22.11.2018
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetAgeOnFuture() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2050, Calendar.FEBRUARY, 1);
        user.setDateOfBirth(calendar.getTime());

        user.getAge();
    }

    /**
     * Test verifying the correctness of the return name and surname
     */
    @Test
    public void testGetFullName() {
        assertEquals(FULL_NAME_ETALONE, user.getFullName());
    }

    @Before
    public void setUp() throws Exception {
        user = new User();
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
    }

    @After
    public void tearDown() throws Exception {

    }
}