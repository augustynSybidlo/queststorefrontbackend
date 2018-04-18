package Controllers;

import DAOs.UsersDao;
import DAOs.UsersDaoImpl;
import Models.Admin;
import Models.Mentor;
import Models.Student;
import Models.User;
import Views.LoginView;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginController implements HttpHandler {
    private LoginView view = new LoginView();
    private UsersDao usersDao = new UsersDaoImpl();
    private ArrayList<User> usersCollection = usersDao.getUsersCollection();

    public void login(String login, String password) {
        boolean isLoginSession = true;

        while (isLoginSession) {
            view.clearScreen();
            String userLogin = login;
            String userPassword = password;
            String userStatus = getUserStatus(userLogin, userPassword);
            if (checkIfUserExists(userLogin) && checkUserPassword(userLogin, userPassword)) {
                runProperUserPanel(userLogin, userPassword, userStatus);
                isLoginSession = false;
            }
        }
    }

    private boolean checkIfUserExists(String login) {
        for (User user : usersCollection) {
            if (login.equals(user.getLogin())) {
                return true;
            }
        }
//        view.displayText("Given user does not exists!");
//        sleepThreadForOneSec();
        return false;
    }

    private boolean checkUserPassword(String login, String password) {
        for (User user : usersCollection) {
            if (login.equals(user.getLogin()) && password.equals(user.getPassword())) {
                return true;
            }
        }
        view.displayText("Wrong password!");
        sleepThreadForOneSec();
        return false;
    }

    private void sleepThreadForOneSec() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

    private String getUserStatus(String login, String password) {
        for (User user : usersCollection){
            if (login.equals(user.getLogin()) && password.equals(user.getPassword())){
                return user.getStatus();
            }
        }
        return null;
    }

    private User getUserAccount(String userLogin, String userPassword) {
        for (User user : usersCollection) {
            if(userLogin.equals(user.getLogin()) && userPassword.equals(user.getPassword())){
                return user;
            }
        }
        return null;
    }

    private void runProperUserPanel(String userLogin, String userPassword, String userStatus) {
        if(userStatus.equals("admin")) {
//            User user = new Admin(userLogin, userPassword, userStatus);
            AdminController controller = new AdminController();
            controller.startAdminPanel();
        }
        else if(userStatus.equals("mentor")) {
            User user = new Mentor(userLogin, userPassword, userStatus);
            MentorController controller = new MentorController();
            controller.startMentorPanel();
        }
        else if(userStatus.equals("student")) {
            Student user = (Student)getUserAccount(userLogin, userPassword);
            StudentController controller = new StudentController();
            controller.startStudentPanel(user);
        }
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            String response = "";
            String method = httpExchange.getRequestMethod();
            System.out.println(method);

            if (method.equals("GET")) {

                // client's address
                String userAgent = httpExchange.getRequestHeaders().getFirst("User-agent");

                // get a template file
                JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/login.html");

                // create a model that will be passed to a template
                JtwigModel model = JtwigModel.newModel();

                // render a template to a string
                response = template.render(model);

                // send the results to a the client
            }

            if(method.equals("POST")){
                InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String formData = br.readLine();

                System.out.println(formData);
                Map<String, String> inputs = parseFormData(formData);
                System.out.println(inputs.toString());
                login(inputs.get("login"), inputs.get("password"));
            }

            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        String[] pairs = formData.split("&");
        for(String pair : pairs){
            String[] keyValue = pair.split("=");
            // We have to decode the value because it's urlencoded. see: https://en.wikipedia.org/wiki/POST_(HTTP)#Use_for_submitting_web_forms
            String value = new URLDecoder().decode(keyValue[1], "UTF-8");
            map.put(keyValue[0], value);
        }
        return map;
    }
}
