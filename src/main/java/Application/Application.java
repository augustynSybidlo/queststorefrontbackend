package Application;

import Controllers.LoginController;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class Application{

    public static void main (String[] argv) throws Exception{

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/login", new LoginController());
        server.createContext("/assets", new AssetsController());

        server.setExecutor(null);
        server.start();
    }




}
