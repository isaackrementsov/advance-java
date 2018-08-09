import com.sun.net.httpserver.*;
import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Arrays;
public class Server {
    public int port;
    private HttpServer server = null;
    public Server(int port){
        this.port = port;
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
        }catch(IOException e){
            System.out.println("Error creating server: " + e);
        }
    }
    public void addController(String url, Controller controller){
        server.createContext(url, controller);
    }
    public void listen(){
        server.start();
    }
}