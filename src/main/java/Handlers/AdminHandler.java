package Handlers;

import Controllers.AdminController;
import Models.Mentor;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.*;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminHandler implements HttpHandler {

//    private static List<Student> students = new ArrayList<>();
    private static AdminController adminController = new AdminController();

    public void handle(HttpExchange httpExchange) throws IOException {

        String response = "";
        String method = httpExchange.getRequestMethod();

        String uri = httpExchange.getRequestURI().getPath();
//        int startIndex = "/adminhome/".length();
//        uri = uri.substring(startIndex);
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/admin_home.twig");
        JtwigModel model = JtwigModel.newModel();

        if (uri.startsWith("addmentor", "/adminhome/".length())){
            System.out.println("uri:" + uri);

            response = parseAddMentorMenu(httpExchange);

            if (method.equals("POST")){
                response = saveAddedMentorAndGoBackToMenu(httpExchange);
            }
        }

        if (response.isEmpty()) {
            response = template.render(model);
        }

        byte[] bytes = response.getBytes("UTF-8");
        httpExchange.sendResponseHeaders(200, bytes.length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(bytes);
        os.close();
        httpExchange.close();
    }

    public String parseAddMentorMenu(HttpExchange httpExchange) throws IOException{

        String method = httpExchange.getRequestMethod();
        String response = "";

        if (method.equals("GET")) {

            JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/add_mentor.twig");
            JtwigModel model = JtwigModel.newModel();

            response = template.render(model);
        }
        return response;
    }

    public String saveAddedMentorAndGoBackToMenu(HttpExchange httpExchange) throws IOException {
        String response = "";

        /* read form data*/
        InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String formData = br.readLine();

        /* parse key=value&another_key=another_value and escaped unicode codepoints into usable form */
        Map<String, String> inputData = parseFormData(formData);
        System.out.println(inputData.get("name"));
        Mentor newMentor = new Mentor(inputData.get("name"), inputData.get("surname"), inputData.get("password"));

        try {
            adminController.getDao().addUserToDatabase(newMentor);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return response;
    }

    private static Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {

        Map<String, String> data = new HashMap<>();

        String[] pairs = formData.split("&");
        for (String pair : pairs) {

            String[] keyValue = pair.split("=");
            String value = new URLDecoder().decode(keyValue[1], "UTF-8");
            data.put(keyValue[0], value);
        }
        return data;
    }
}