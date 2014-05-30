package Server;

import accountServcie.AccountService;
import frontend.Frontend;
import dbService.ProductionDataService;


import game.GameMechanics;
import messageSystem.MessageSystem;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import utils.resource.ResourceFactory;
import utils.resource.ServerRes;
import utils.vfs.VFS;
import utils.vfs.VirtualFileSystem;

import java.util.Iterator;

/**
 * Created by Andrey
 * 15.02.14.
 *
 */
public class Main {
    private static ServerRes serverRes = (ServerRes) ResourceFactory.getInstance().get("data/server.xml");

    public static void main(String[] args) throws Exception
    {
        VirtualFileSystem vfs = new VFS(System.getProperty("user.dir"));
        Iterator<String> resources = vfs.getIterator("data");
        while (resources.hasNext()){
            String nextFile = resources.next();
            System.out.println(nextFile);
            if (!vfs.isDirectory(nextFile)){
                ResourceFactory.getInstance().add(nextFile, vfs.getAbsolutePath(nextFile));
            }
        }

        MessageSystem messageSystem = new MessageSystem();
        new Thread(new AccountService(new ProductionDataService(), messageSystem)).start();
        new Thread(new AccountService(new ProductionDataService(), messageSystem)).start();
        new Thread(new GameMechanics(messageSystem)).start();

        Frontend frontend = new Frontend(messageSystem);
        new Thread(frontend).start();

        Server server = new Server(serverRes.getPort());
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(frontend), "/*");

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setResourceBase("static");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});
        server.setHandler(handlers);

        server.start();
        server.join();
    }
}