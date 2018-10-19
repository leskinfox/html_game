package servlets;

import services.Services;
import services.players.Battle;
import services.players.Player;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GateServlet extends HttpServlet {
    private final Services services;

    public GateServlet(Services services) {
        this.services = services;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession().getId();
        services.statistics.reset(sessionId);
        services.statistics.startPageTimer(sessionId);
        Player player = services.players.get(sessionId);
        String page;
        resp.setCharacterEncoding("UTF-8");
        if (player == null) {
            resp.sendRedirect("/");
        }
        player.exit();
        for (int i = 0; i<300; i++) {
            long startTime = System.currentTimeMillis();
            player.setReady(true);
            while(System.currentTimeMillis()-startTime < 10 && player.isReady());
            if (player.hasBattle())
                break;
            player.setReady(false);
            if(services.players.searchEnemy(player))
                break;
        }
        if (player.hasBattle()) {
            Map<String, String> params = new HashMap<>();
            Battle battle = player.getBattle();
            int playerId = player.getIdInBattle();
            page = services.template.getPage(sessionId,"wait.html", getParams(battle, playerId));
        }
        else
            page = services.template.getPage(sessionId,"not_found.html", new HashMap<>());
        resp.getWriter().println(page);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private HashMap<String, String> getParams (Battle battle, int playerId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("enemy_name", battle.getEnemyName(playerId));
        params.put("my_name", battle.getMyName(playerId));
        params.put("my_life", Long.toString(battle.getMyLife(playerId)));
        params.put("my_damage", Long.toString(battle.getMyDamage(playerId)));
        params.put("my_rating", Long.toString(battle.getMyRating(playerId)));
        params.put("his_name", battle.getEnemyName(playerId));
        params.put("his_rating", Long.toString(battle.getEnemyRating(playerId)));
        return params;
    }
}

