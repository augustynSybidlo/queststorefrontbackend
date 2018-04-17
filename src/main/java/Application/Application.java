package Application;

import Controllers.LoginController;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Application{

    public static void main (String[] argv) throws Exception{

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/login", new LoginController());
        server.setExecutor(null);
        server.start();
    }



//    private void startApp(){
//        LoginController loginProcedure = new LoginController();
//        loginProcedure.login();
//    }
}
