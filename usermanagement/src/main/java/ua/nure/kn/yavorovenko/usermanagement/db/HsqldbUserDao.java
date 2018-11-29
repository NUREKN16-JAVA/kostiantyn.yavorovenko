package ua.nure.kn.yavorovenko.usermanagement.db;

import ua.nure.kn.yavorovenko.usermanagement.User;

import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;

class HsqldbUserDao implements UserDao {
    private static final String CREATE_USER_SQL = "INSERT INTO PUBLIC.USERS (firstname, lastname, dateofbirth) VALUES (?, ?, ?)";
    private static final String UPDATE_USER_SQL = "UPDATE PUBLIC.USERS SET firstname = ?, lastname = ?, dateofbirth = ? WHERE id = ?";
    private static final String DELETE_USER_SQL = "DELETE FROM PUBLIC.USERS WHERE id = ?";
    private static final String FIND_USER_SQL = "SELECT firstname, lastname, dateofbirth FROM PUBLIC.USERS WHERE id = ?";
    private static final String FIND_ALL_SQL = "SELECT id, firstname, lastname, dateofbirth FROM PUBLIC.USERS";

    private static final String IDENTITY_SQL = "CALL IDENTITY()";

    private ConnectionFactory connectionFactory;

    public HsqldbUserDao() {
    }

    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public User create(User user) throws DatabaseException {
        try (Connection connection = connectionFactory.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_USER_SQL);
             CallableStatement callableStatement = connection.prepareCall(IDENTITY_SQL)) {

            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setDate(3, new Date(user.getDateOfBirth().getTime()));

            int result = preparedStatement.executeUpdate();

            if (result != 1) {
                throw new DatabaseException("Number of the inserted rows: " + result);
            }

            ResultSet keys = callableStatement.executeQuery();
            if (keys.next()) {
                user.setId(keys.getLong(1));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Database has some errors", e);
        }
        return user;
    }

    @Override
    public void update(User user) throws DatabaseException {
        try (Connection connection = connectionFactory.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_SQL)) {

            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setDate(3, new Date(user.getDateOfBirth().getTime()));
            preparedStatement.setLong(4, user.getId());

            int result = preparedStatement.executeUpdate();

            if (result != 1) {
                throw new DatabaseException("Number of the updated rows: " + result);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database has some errors", e);
        }
    }

    @Override
    public void delete(User user) throws DatabaseException {
        try (Connection connection = connectionFactory.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_SQL)) {

            preparedStatement.setLong(1, user.getId());

            int result = preparedStatement.executeUpdate();

            if (result != 1) {
                throw new DatabaseException("Number of the deleted rows: " + result);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database has some errors", e);
        }
    }

    @Override
    public User find(Long id) throws DatabaseException {
        User user = null;
        try (Connection connection = connectionFactory.createConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_USER_SQL)) {
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                user = new User(id,
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getDate(3)
                );
            } else {
                throw new DatabaseException("The user does not exist!");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Database has some errors", e);
        }
        return user;
    }

    @Override
    public Collection<User> findAll() throws DatabaseException {
        Collection<User> users = new LinkedList<>();

        try (Connection connection = connectionFactory.createConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(FIND_ALL_SQL);

            while (resultSet.next()) {
                User user = new User(
                        resultSet.getLong(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getDate(4)
                );
                users.add(user);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Database has some errors", e);
        }

        return users;
    }
}
