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
        if (player == null) {
            resp.sendRedirect("/");
        }
        player.exit();
        for (int i = 0; i<1000; i++) {
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
            params.put("enemy_name", battle.getEnemyName(playerId));
            page = services.template.getPage(sessionId,"wait.html", params);
        }
        else
            page = "Нет пользователей попробуйте позже";
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().println(page);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
