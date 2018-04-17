package Handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminHandler implements HttpHandler {

//    private static List<Student> students = new ArrayList<>();

    public void handle(HttpExchange httpExchange) throws IOException {

        String response = "";

        String method = httpExchange.getRequestMethod();

        String uri = httpExchange.getRequestURI().getPath();
        int startIndex = "/adminhome/".length();
        uri = uri.substring(startIndex);
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/admin_home.twig");
        JtwigModel model = JtwigModel.newModel();
        response = template.render(model);


        byte[] bytes = response.getBytes("UTF-8");
        httpExchange.sendResponseHeaders(200, bytes.length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(bytes);
        os.close();
        httpExchange.close();
    }}