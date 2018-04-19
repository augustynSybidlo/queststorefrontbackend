package Application;

import Controllers.LoginController;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

import Handlers.AdminHandler;

    public class Application {

    public static void main (String[] argv) throws Exception{

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/login", new LoginController());
        server.createContext("/assets", new AssetsController());
        server.createContext("/adminhome", new AdminHandler());
        server.setExecutor(null);
        server.start();
    }




}
