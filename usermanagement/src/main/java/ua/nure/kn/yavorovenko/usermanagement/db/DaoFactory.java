package ua.nure.kn.yavorovenko.usermanagement.db;

import java.io.IOException;
import java.util.Properties;

public abstract class DaoFactory {
    private static DaoFactory instance;
    private static final String DAO_FACTORY = "dao.factory";
    protected static final String USER_DAO_IMPLEMENTATION = "ua.nure.kn.yavorovenko.usermanagement.db.UserDao";
    protected static Properties properties;

    static {
        properties = new Properties();
        try {
            properties.load(DaoFactory.class.getClassLoader().getResourceAsStream("settings.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Can not create ", e);
        }
    }

    protected DaoFactory() {

    }

    public static void init(Properties customProperties) {
        properties = customProperties;
    }

    public static synchronized DaoFactory getInstance() {
        if (instance == null){
            try {
                Class factoryClass = Class.forName(properties
                        .getProperty(DAO_FACTORY));
                instance = (DaoFactory) factoryClass.newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return instance;
    }

    public ConnectionFactory getConnectionFactory() {
        return new ConnectionFactoryImplementation(properties);
    }

    public abstract UserDao getUserDao();

}
