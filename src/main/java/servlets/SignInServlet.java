package servlets;

import services.Services;
import services.db.DBService;
import services.db.UsersDataSet;
import org.apache.commons.codec.digest.DigestUtils;
import services.players.Player;
import services.players.PlayersService;
import services.statistics.StatisticsService;
import services.template.TemplateService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SignInServlet extends HttpServlet {

    private final Services services;

    public SignInServlet(Services services) {
        this.services = services;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession().getId();
        services.statistics.reset(sessionId);
        services.statistics.startPageTimer(sessionId);
        resp.setCharacterEncoding("UTF-8");

        if(services.players.exist(sessionId)) {
            String page = services.template.getPage(sessionId, "menu.html", new HashMap<>());
            resp.getWriter().println(page);
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        long beginTime = System.currentTimeMillis();
        String page = services.template.getPage(sessionId, "signin.html", new HashMap<>());
        resp.getWriter().println(page);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession().getId();
        services.statistics.reset(sessionId);
        services.statistics.startPageTimer(sessionId);
        String login = req.getParameter("login");
        String password = DigestUtils.md5Hex(req.getParameter("password"));
        resp.setCharacterEncoding("UTF-8");
        UsersDataSet user = null;
        try {
            user = services.db.getUserByLogin(sessionId, login);
            if (user == null) {
                services.db.addUser(sessionId, login, password, 0L, 100L, 10L);
                user = services.db.getUserByLogin(sessionId, login);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        if (!user.getPassword().equals(password)) {
            resp.getWriter().println("Ошибка авторизации");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        Player player = new Player(services.db, sessionId, user);
        services.players.add(req.getSession().getId(), player);
        String page = services.template.getPage(sessionId, "menu.html", new HashMap<>());
        resp.getWriter().println(page);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
