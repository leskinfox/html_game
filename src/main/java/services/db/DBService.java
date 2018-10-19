package services.db;

import services.statistics.StatisticsService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBService {
    private final Connection connection;
    private final StatisticsService statistics;

    public DBService(StatisticsService statistics) throws SQLException {
        this.statistics = statistics;
        StringBuilder url = new StringBuilder();
        url.
                append("jdbc:mysql://").                    //services.db type
                append("localhost:").                       //host name
                append("3306/").                            //port
                append("html_game?").                      //services.db name
                append("user=root&").                      //login
                append("password=&").                     //password
                append("useSSL=false&").
                append("serverTimezone=UTC");
        System.out.println("URL: " + url + "\n");
        connection = DriverManager.getConnection(url.toString());
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

    public long addUser(String sessionID, String login, String password, long rating, long life, long damage) throws SQLException {
        statistics.startDBTimer(sessionID);
        UsersDAO dao = new UsersDAO(connection);
        statistics.addRequest(sessionID);
        dao.insertUser(login, password, rating, life, damage);
        statistics.addRequest(sessionID);
        long id= dao.getUserId(login);
        statistics.stopDBTimer(sessionID);
        return id;
    }






}
