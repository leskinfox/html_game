package services.db;

import services.statistics.StatisticsService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBService {
    private final Connection connection;
    private final StatisticsService statistics;

    public DBService(StatisticsService statistics) throws SQLException, IOException {

        String HOST;
        String PORT;
        String DB_NAME;
        String USER;
        String PASSWORD;
        String USE_SSL;

        Properties props = new Properties();
        props.load(new FileInputStream(new File("my_sql.ini")));

        HOST = props.getProperty("HOST", "localhost");
        PORT = props.getProperty("PORT", "3306");
        DB_NAME = props.getProperty("DB_NAME", "");
        USER = props.getProperty("USER", "");
        PASSWORD = props.getProperty("PASSWORD", "");
        USE_SSL = props.getProperty("USE_SSL", "false");

        this.statistics = statistics;
        StringBuilder url = new StringBuilder();
        url.
                append("jdbc:mysql://").
                append(HOST).append(":").
                append(PORT).append("/").
                append(DB_NAME).append("?").
                append("useSSL=").append(USE_SSL).append("&").
                append("serverTimezone=UTC");
        Properties properties = new Properties();
        properties.setProperty("user", USER);
        properties.setProperty("password", PASSWORD);
        properties.setProperty("autoReconnect", "true");
        properties.setProperty("connectTimeout", "1000");
        properties.setProperty("socketTimeout", "1000");
        properties.setProperty("maxReconnects", "1");
        properties.setProperty("retriesAllDown", "1");
        connection = DriverManager.getConnection(url.toString(), properties);
        UsersDAO dao = new UsersDAO(connection);
        dao.createTable();
    }

    public UsersDataSet getUserByLogin(String sessionID, String login) throws SQLException {
        statistics.startDBTimer(sessionID);
        UsersDataSet usersDataSet = new UsersDAO(connection).getUserByLogin(login);
        statistics.stopDBTimer(sessionID);
        statistics.addRequest(sessionID);
        return usersDataSet;
    }

    public void updateUser(String sessionID, String login, long rating, long life, long damage) throws SQLException {
        statistics.startDBTimer(sessionID);
        UsersDAO dao = new UsersDAO(connection);
        dao.updateUserByLogin(login, rating, life, damage);
        statistics.stopDBTimer(sessionID);
        statistics.addRequest(sessionID);
    }

    public void addUser(String sessionID, String login, String password, long rating, long life, long damage) throws SQLException {
        statistics.startDBTimer(sessionID);
        UsersDAO dao = new UsersDAO(connection);
        statistics.addRequest(sessionID);
        dao.insertUser(login, password, rating, life, damage);
        statistics.stopDBTimer(sessionID);
    }






}
