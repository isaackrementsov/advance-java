import java.net.*;
import java.io.*;
import java.util.HashMap;
import com.sun.net.httpserver.*;
public abstract class Controller implements HttpHandler {
    HashMap<String, Object> data = new HashMap<>();
    OutputStream res;
    HashMap<String, String> params = new HashMap<>();
    //HashMap<String, String> query = new HashMap<>(); 
    Param[] rules;
    public Controller(){
        this.data = null;
    }
    public Controller(HashMap<String, Object> data){
        this.parseQuery = parseQuery;
        this.data = data;
    }
    private HashMap<String, String> parseParams(String url){
        HashMap<String, String> params = new HashMap<>();
        for(Param p : this.paramRules){
            int end;
            int slashIndex = url.substring(p.start).indexOf("/");
            if(slashIndex == -1){
                end = url.length(); 
            }else{
                end = slashIndex + p.start;
            }
            params.put(p.name, url.substring(p.start, end));
        }
        return params;
    }
    public void handle(HttpExchange he){
        res = he.getResponseBody();
        String method = he.getRequestMethod();
        this.params = parseParams(he.getRequestURI().path);
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