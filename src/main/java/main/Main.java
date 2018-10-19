package main;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import services.Services;
import servlets.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Services services = new Services();

        SignInServlet signInServlet = new SignInServlet(services);
        SignOutServlet signOutServlet = new SignOutServlet(services);
        MenuServlet menuServlet = new MenuServlet(services);
        GateServlet gateServlet = new GateServlet(services);
        DuelServlet duelServlet = new DuelServlet(services);
        NotFoundServlet notFoundServlet = new NotFoundServlet(services);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(notFoundServlet), "/*");
        context.addServlet(new ServletHolder(signInServlet), "");
        context.addServlet(new ServletHolder(signOutServlet), "/signout");
        context.addServlet(new ServletHolder(menuServlet), "/menu");
        context.addServlet(new ServletHolder(gateServlet), "/gate");
        context.addServlet(new ServletHolder(duelServlet), "/duel");

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase("public");
        resourceHandler.setDirectoriesListed(false);
        ContextHandler context2 = new ContextHandler("/public");
        context2.setHandler(resourceHandler);




        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{context2, context});

        Server server = new Server(8080);
        server.setHandler(handlers);

        server.start();
        server.join();
    }
}
