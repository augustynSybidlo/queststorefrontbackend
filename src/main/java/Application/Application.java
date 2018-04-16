package Application;

import Controllers.LoginController;

public class Application{

    public static void main (String[] argv){
        Application app = new Application();
        app.startApp();
    }

    private void startApp(){
        LoginController loginProcedure = new LoginController();
        loginProcedure.login();
    }
}
