package Controllers;

import DAOs.UsersDao;
import DAOs.UsersDaoImpl;
import Models.Student;
import Models.User;
import Views.LoginView;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import com.sun.xml.internal.bind.v2.TODO;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.*;
import java.net.HttpCookie;
import java.net.URLDecoder;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginController implements HttpHandler {

    private List<String> logins = Arrays.asList("admin");
    private List<String> passwords = Arrays.asList("admin");
    private Map<String, String> sessions = new HashMap<String, String>();
    private int counter = 0;

    private LoginView view = new LoginView();
    private UsersDao usersDao = new UsersDaoImpl();
    private ArrayList<User> usersCollection = usersDao.getUsersCollection();

    public boolean login(String login, String password, HttpExchange httpExchange) {
        boolean isValid = false;

        view.clearScreen();
        String userLogin = login;
        String userPassword = password;
        String userStatus = getUserStatus(userLogin, userPassword);
        if (checkIfUserExists(userLogin) && checkUserPassword(userLogin, userPassword)) {
            runProperUserPanel(userLogin, userPassword, userStatus, httpExchange);
            isValid = true;
        }
        return isValid;
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
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private String getUserStatus(String login, String password) {
        for (User user : usersCollection) {
            if (login.equals(user.getLogin()) && password.equals(user.getPassword())) {
                return user.getStatus();
            }
        }
        return null;
    }

    private User getUserAccount(String userLogin, String userPassword) {
        for (User user : usersCollection) {
            if (userLogin.equals(user.getLogin()) && userPassword.equals(user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    private void runProperUserPanel(String userLogin, String userPassword, String userStatus, HttpExchange httpExchange) {
        if(userStatus.equals("admin")) {
            redirectTo(httpExchange, "/adminhome");
//        } else if (userStatus.equals("mentor")) {
//            User user = new Mentor(userLogin, userPassword, userStatus);
//            MentorController controller = new MentorController();
//            controller.startMentorPanel();
//        } else if (userStatus.equals("student")) {
//            Student user = (Student) getUserAccount(userLogin, userPassword);
//            StudentController controller = new StudentController();
//            controller.startStudentPanel(user);
        }
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "";
        String method = httpExchange.getRequestMethod();


        if (method.equals("GET")) {

//            String userAgent = httpExchange.getRequestHeaders().getFirst("User-agent");


            String sessionID = getCookie("sessionID", httpExchange);

            if (sessionID == null || sessionID.equals("\"\"")) {
                JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/login.html");
                JtwigModel model = JtwigModel.newModel();
                response = template.render(model);
            } else {
                String login = sessions.get(sessionID);

                if (login == null) {
                    JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/login.html");
                    JtwigModel model = JtwigModel.newModel();
//                    model.with("validationInfo", "Not valid session. Please log in again!");
                    response = template.render(model);
                }
            }
        }

        if (method.equals("POST")) {

            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String formData = br.readLine();

            Map<String, String> inputs = parseFormData(formData);
            System.out.println(inputs.toString());
            boolean isLoginCorrect = login(inputs.get("login"), inputs.get("password"), httpExchange);
            System.out.println(inputs.get("login"));
            System.out.println(inputs.get("password"));

            if (isLoginCorrect) {

                String sessionID = String.valueOf(counter++);
                setCookie("sessionID", sessionID, httpExchange);
                sessions.put(sessionID, inputs.get("login"));
                System.out.println(String.valueOf(counter));

                } else {
                    JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/login.html");
                    JtwigModel model = JtwigModel.newModel();
                    model.with("validationInfo", "Password incorrect. Please log in again!");
                    response = template.render(model);
                }
            }
//                } else {
//                    //for tests
////                    response = "Not recognized user. Please log in again.";
////                    System.out.println(response);
//                    JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/login.html");
//                    JtwigModel model = JtwigModel.newModel();
//                    model.with("validationInfo", "Not recognized user. Please log in again!");
//                    response = template.render(model);
//                }
//            } else {
//                // logout
//                String sessionID = getCookie("sessionID", httpExchange);
//                if (sessionID != null) {
//                    sessions.remove(sessionID);
//                    System.out.println("You have been successfully logged out.");
//                    JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/login.html");
//                    JtwigModel model = JtwigModel.newModel();
//                    response = template.render(model);
//                    deleteCookie("sessionID", httpExchange);
//                }
//            }
//        }
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }


    private void deleteCookie(String name, HttpExchange httpExchange) {
        HttpCookie cookie = new HttpCookie(name, "");
        cookie.setMaxAge(0);
        httpExchange.getResponseHeaders().add("Set-Cookie", cookie.toString());
    }

    private String getCookie(String nameCookie, HttpExchange httpExchange) {

        String cookieHeader = httpExchange.getRequestHeaders().getFirst("Cookie");
        System.out.println("cookieHeader: " + cookieHeader);

        if (cookieHeader == null) {
            return null;
        }

        String[] split = cookieHeader.split(";");
        for (String cookieString : split) {
            HttpCookie cookie = HttpCookie.parse(cookieString).get(0);
            if (cookie.getName().equals(nameCookie)) {
                return cookie.getValue();
            }

        }
        return null;
    }

    private void setCookie(String name, String value, HttpExchange httpExchange) {
        HttpCookie cookie = new HttpCookie(name, value); // This isn't a good way to create sessionId. Find out better!
        httpExchange.getResponseHeaders().add("Set-Cookie", cookie.toString());

    }

    private static Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<String, String>();
        String[] pairs = formData.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");

            String value = new URLDecoder().decode(keyValue[1], "UTF-8");
            map.put(keyValue[0], value);
        }
        return map;
    }

    private void redirectTo(HttpExchange httpExchange, String destination) {
        try {
            Headers responseHeaders = httpExchange.getResponseHeaders();
            responseHeaders.add("Location", destination);
            httpExchange.sendResponseHeaders(302, -1);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
