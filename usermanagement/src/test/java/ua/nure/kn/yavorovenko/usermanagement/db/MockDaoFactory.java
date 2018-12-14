package ua.nure.kn.yavorovenko.usermanagement.db;

import com.mockobjects.dynamic.Mock;

public class MockDaoFactory extends DaoFactory {

    private static final Class<UserDao> MOCKED_CLASS = UserDao.class;

    private Mock mockUserDao;

    public Mock getMockUserDao() {
        return mockUserDao;
    }

    public MockDaoFactory() {
        this.mockUserDao = new Mock(MOCKED_CLASS);
    }

    @Override
    public UserDao getUserDao() {
        return (UserDao) mockUserDao.proxy();
    }
}
