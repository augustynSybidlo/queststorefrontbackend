package Application;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AssetsController implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {

            String requestPath = httpExchange.getRequestURI().getPath();

            File homedir = new File(System.getProperty("user.dir"));
            File filePath = new File(homedir, "/src/main/resources/" + requestPath);

            byte[] response = readFile(filePath.toString());

            httpExchange.sendResponseHeaders(200, response.length);

            OutputStream os = httpExchange.getResponseBody();
            os.write(response);
            os.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    static byte[] readFile(String path)
            throws IOException
    {
        return Files.readAllBytes(Paths.get(path));
    }


}
