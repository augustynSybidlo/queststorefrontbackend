package Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.io.OutputStream;

public class MentorHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
//        String response = "";
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/mentor_home.twig");
        JtwigModel model = JtwigModel.newModel();

//        if (response.isEmpty()) {
//            response = template.render(model);
//        }
        String response = template.render(model);
//        byte[] bytes = response.getBytes("UTF-8");
//        httpExchange.sendResponseHeaders(200, bytes.length);
//        OutputStream os = httpExchange.getResponseBody();
//        os.write(bytes);
//        os.close();
//        httpExchange.close();

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
