import java.net.*;
import java.io.*;
import java.util.HashMap;
import com.sun.net.httpserver.*;
public abstract class Controller implements HttpHandler {
    private HashMap<String, Object> data = new HashMap<>();
    private OutputStream res;
    private String[] params;
    public Controller(){
        this.data = null;
    }
    public Controller(HashMap<String, Object> data){
        this.data = data;
    }
    public void handle(HttpExchange he){
        res = he.getResponseBody();
        String method = he.getRequestMethod();
        String path = he.getRequestURI().replace(url, "")
        String[] reqParams = path.split("/")
        int i = 0;
        while(i < reqParams.length){
            reqParams[i] = reqParams[i].replace("/", "")
            i++
        }
        params = reqParams;
        try {
            switch(method){
                case "GET":
                    this.get(he);
                case "POST":
                    this.post(he);
                case "PUT":
                    this.put(he);
                case "PATCH":
                    this.patch(he);
                case "DELETE":
                    this.delete(he);
            }        
        }catch(Exception e){
            System.out.println("Controller exception: " + e);
        }
    
    }
    private void get(HttpExchange he) throws Exception {
        he.sendResponseHeaders(405, 0);
    }
    private void post(HttpExchange he) throws Exception {
        he.sendResponseHeaders(405, 0);
    } 
    private void put(HttpExchange he) throws Exception {
        he.sendResponseHeaders(405, 0);
    } 
    private void patch(HttpExchange he) throws Exception {
        he.sendResponseHeaders(405, 0);
    } 
    private void delete(HttpExchange he) throws Exception {
        he.sendResponseHeaders(405, 0);
    }
}