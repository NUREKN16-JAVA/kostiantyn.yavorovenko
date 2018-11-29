package ua.nure.kn.yavorovenko.usermanagement.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactoryImplementation implements ConnectionFactory {
    private String driver;
    private String url;
    private String user;
    private String password;


    public ConnectionFactoryImplementation(String driver, String url, String user, String password) {
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    private ConnectionFactoryImplementation() {
    }

    @Override
    public Connection createConnection() throws DatabaseException {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot load the driver class!", e);
        }

        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new DatabaseException("Cannot get connection from database!", e);
        }
    }
}
