package ua.nure.kn.yavorovenko.usermanagement.db;

import java.io.IOException;
import java.util.Properties;

public class DaoFactory {
    private final static DaoFactory INSTANCE = new DaoFactory();
    private static final String USER_DAO_IMPLEMENTATION = "ua.nure.kn.yavorovenko.usermanagement.db.UserDao";
    private final Properties properties;

    private DaoFactory() {
        this.properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("settings.properties"));

        } catch (IOException e) {
            throw new RuntimeException("Can not create ", e);
        }
    }

    public static DaoFactory getInstance() {
        return INSTANCE;
    }

    public ConnectionFactory getConnectionFactory() {
        String driver = properties.getProperty("connection.driver");
        String user = properties.getProperty("connection.user");
        String password = properties.getProperty("connection.password");
        String url = properties.getProperty("connection.url");

        return new ConnectionFactoryImplementation(driver, url, user, password);
    }

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
