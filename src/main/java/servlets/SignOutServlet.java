package servlets;

import services.Services;
import services.players.Player;
import services.players.PlayersService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class SignOutServlet extends HttpServlet {
    private final Services services;

    public SignOutServlet (Services services) {
        this.services = services;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession().getId();
        services.statistics.reset(sessionId);
        services.statistics.startPageTimer(sessionId);
        if(services.players.exist(sessionId)) {
            Player player = services.players.get(sessionId);
            player.exit();
            services.players.remove(sessionId);
        }
        String page = services.template.getPage(sessionId, "signin.html", new HashMap<>());
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().println(page);
        resp.setStatus(HttpServletResponse.SC_OK);
    }


}
