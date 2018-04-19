package Handlers;

import Controllers.AdminController;
import Models.Group;
import Models.Mentor;
import Models.User;
import com.sun.net.httpserver.Headers;
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
    private static List<User> mentors = adminController.getAllMentors();

    public void handle(HttpExchange httpExchange) throws IOException {

        String response = "";
        String method = httpExchange.getRequestMethod();

        String uri = httpExchange.getRequestURI().getPath();
//        int startIndex = "/adminhome/".length();
//        uri = uri.substring(startIndex);
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/admin_home.twig");
        JtwigModel model = JtwigModel.newModel();

        if (uri.startsWith("addmentor", "/adminhome/".length())){

            response = parseAddMentorMenu(httpExchange);

            if (method.equals("POST")){
                response = saveAddedMentorAndGoBackToMenu(httpExchange);
            }
        }
        else if (uri.startsWith("addgroup", "/adminhome/".length())){
            response = parseAddGroupMentorMenu(httpExchange);

            if (method.equals("POST")){
                response = saveAddedGroup(httpExchange);
            }
        }

        else if (uri.startsWith("assignmentor", "/adminhome/".length())){
            response = parseChoosingMentorMenu(httpExchange);

            if (method.equals("POST")){
                Integer mentorId = goToGroupChoosingMenuAfterChoosingMentor(httpExchange);

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

    private String parseChoosingMentorMenu(HttpExchange httpExchange) {
        String method = httpExchange.getRequestMethod();
        String response = "";

        if (method.equals("GET")) {
            JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/choose_mentor.twig");

            List<String> rowCollection = new ArrayList<>();

            for (User mentor : mentors){
                String current = "<tr>" +
                        "    <td>" + mentor.toString() + "</td>" +
                        "</tr>";
                rowCollection.add(current);
            }

            JtwigModel model = JtwigModel.newModel().with("mentors", rowCollection);
            response = template.render(model);
        }
        return response;
    }

    public Integer goToGroupChoosingMenuAfterChoosingMentor(HttpExchange httpExchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String formData = br.readLine();

        Map<String, String> inputData = parseFormData(formData);
        Integer mentorId = (Integer.parseInt(inputData.get("name")));

        return mentorId;
    }

    public String parseChoosingGroupMenu(HttpExchange httpExchange, Integer mentorId){
        return "";
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

    public String parseAddGroupMentorMenu(HttpExchange httpExchange) throws IOException{

        String method = httpExchange.getRequestMethod();
        String response = "";

        if (method.equals("GET")) {

            JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/add_group.twig");
            JtwigModel model = JtwigModel.newModel()/*.with("fiels", fieldCollection) podobnie jak index*/;

            response = template.render(model);
        }
        return response;
    }

    public String saveAddedGroup(HttpExchange httpExchange) throws IOException{

        String response = "";

        /* read form data*/
        InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String formData = br.readLine();

        /* parse key=value&another_key=another_value and escaped unicode codepoints into usable form */
        Map<String, String> inputData = parseFormData(formData);

        try {
            adminController.createNewGroup(inputData.get("name"));
        }
        catch (SQLException e){
            e.printStackTrace();
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

        try {
            adminController.createNewMentor(inputData.get("name"), inputData.get("surname"), inputData.get("password"));
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