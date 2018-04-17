import Handlers.AdminHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

    public static void main(String[] args) {

        // create a server on port 8000
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(8000), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // set routes
//        server.createContext("/", new StudentHandler());
        server.createContext("/adminhome", new AdminHandler());
        server.setExecutor(null); // creates a default executor

        // start listening
        server.start();
    }
}
