package ua.nure.kn.yavorovenko.usermanagement.db;

public class DaoFactoryImplementation extends  DaoFactory{

    public UserDao getUserDao() {
        UserDao userDao = null;

        try {
            Class userDaoImplementationClass = Class
                    .forName(properties
                            .getProperty(USER_DAO_IMPLEMENTATION));

            userDao = (UserDao) userDaoImplementationClass.newInstance();
            userDao.setConnectionFactory(getConnectionFactory());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("Cannot load the DAO implementation class!", e);
        }

        return userDao;
    }
}
