package servlets;

import services.Services;
import services.players.Player;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        resp.sendRedirect("/");
        resp.setStatus(HttpServletResponse.SC_OK);
    }


}
