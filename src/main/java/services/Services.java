package services;

import services.db.DBService;
import services.players.PlayersService;
import services.statistics.StatisticsService;
import services.template.TemplateService;

import java.sql.SQLException;

public class Services {
    public DBService db;
    public final PlayersService players;
    public final StatisticsService statistics;
    public final TemplateService template;

    public Services() {
        statistics = new StatisticsService();
        template = new TemplateService(statistics);
        players = new PlayersService();
        try {
            db = new DBService(statistics);
        } catch (SQLException e) {
            e.printStackTrace();
            db = null;
        }
    }
}
