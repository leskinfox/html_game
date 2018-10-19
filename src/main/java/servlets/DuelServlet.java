package servlets;

import services.Services;
import services.players.Battle;
import services.players.Player;
import services.players.PlayersService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DuelServlet extends HttpServlet {
    private final Services services;

    public DuelServlet(Services services) {
        this.services = services;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession().getId();
        services.statistics.reset(sessionId);
        services.statistics.startPageTimer(sessionId);
        Player player = services.players.get(sessionId);
        if (player==null || !player.hasBattle() || !player.getBattle().isStartGame()) {
            resp.sendRedirect("/");
            return;
        }
        Battle battle = player.getBattle();
        int playerId = player.getIdInBattle();

        if (req.getParameter("attack") != null)
            battle.attack(playerId);

        String page = services.template.getPage(sessionId,"duel.html", getParams(battle, playerId));
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().println(page);
        resp.setStatus(HttpServletResponse.SC_OK);
    }


    private HashMap<String, String> getParams (Battle battle, int playerId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("my_name", battle.getMyName(playerId));
        params.put("my_health", Long.toString(battle.getMyHealth(playerId)));
        params.put("my_damage", Long.toString(battle.getMyDamage(playerId)));
        params.put("my_rating", Long.toString(battle.getMyRating(playerId)));
        params.put("his_name", battle.getEnemyName(playerId));
        params.put("his_health", Long.toString(battle.getEnemyHealth(playerId)));
        params.put("his_damage", Long.toString(battle.getEnemyDamage(playerId)));
        params.put("his_rating", Long.toString(battle.getEnemyRating(playerId)));
        params.put("log", battle.getLog(playerId));
        return params;
    }
}

