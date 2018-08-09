import java.net.*;
import java.io.*;
import java.util.HashMap;
import com.sun.net.httpserver.*;
public class MainController extends Controller {
    void get(HttpExchange he) throws IOException {
        String response = "Server listening on port 8081";
        he.sendResponseHeaders(200, response.length());
        res.write(response.getBytes());
        res.close();
    }
}