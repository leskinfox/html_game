package services.db;

import java.sql.*;

public class UsersDAO {

    private final Connection connection;

    public  UsersDAO(Connection connection) {
        this.connection = connection;
    }

    public void insertUser (String login, String password, long rating, long life, long damage) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO USERS (LOGIN, PASSWORD, RATING, LIFE, DAMAGE) VALUES (?, ?, ?, ?, ?)");
        stmt.setString(1, login);
        stmt.setString(2, password);
        stmt.setLong(3, rating);
        stmt.setLong(4, life);
        stmt.setLong(5, damage);
        stmt.executeUpdate();
        stmt.close();
    }

    public long getUserId(String login) throws  SQLException {
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT ID FROM USERS WHERE LOGIN = ?");
        stmt.setString(1, login);
        stmt.executeQuery();
        ResultSet result = stmt.getResultSet();
        long id;
        if(result.next())
            id = result.getLong(1);
        id = 0;
        result.close();
        stmt.close();
        return id;
    }

    public UsersDataSet getUserByLogin(String login) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM USERS WHERE LOGIN = ?");
        stmt.setString(1, login);
        stmt.executeQuery();
        ResultSet result = stmt.getResultSet();
        UsersDataSet user;
        if(result.next())
            user = new UsersDataSet(
                    result.getLong(1),     //id
                    result.getString(2),   //login
                    result.getString(3),   //password
                    result.getLong(4),     //rating
                    result.getLong(5),     //life
                    result.getLong(6)     //damage
            );
        else
            user = null;
        result.close();
        stmt.close();
        return user;
    }

    public void updateUserByLogin(String login, long newRating, long newLife, long newDamage) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(
                "UPDATE USERS SET RATING = ?, LIFE = ?, DAMAGE = ? WHERE LOGIN = ?");
        stmt.setLong(1, newRating);
        stmt.setLong(2, newLife);
        stmt.setLong(3, newDamage);
        stmt.setString(4, login);
        stmt.executeUpdate();
        stmt.close();
    }

    public void createTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(
                "CREATE TABLE IF NOT EXISTS USERS (ID BIGINT AUTO_INCREMENT, LOGIN VARCHAR(256), "
                        + "PASSWORD VARCHAR(256), RATING BIGINT, LIFE BIGINT, DAMAGE BIGINT, PRIMARY KEY (ID))");
        stmt.close();
    }
}
