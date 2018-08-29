import java.net.*;
import java.io.*;
import java.util.HashMap;
import com.sun.net.httpserver.*;
package advance;
public abstract class Controller implements HttpHandler {
    HashMap<String, Object> data = new HashMap<>();
    OutputStream res;
    HashMap<String, String> params = new HashMap<>();
    HashMap<String, String> query = new HashMap<>(); 
    Server.Param[] rules;
    private HashMap<String, String> parseQuery(URI url) throws UnsupportedEncodingException {
        HashMap<String, String> queryPairs = new HashMap<String, String>();
        String query = url.getQuery();
        String[] pairs = query.split("&");
        for(String pair : pairs) {
            int i = pair.indexOf("=");
            queryPairs.put(URLDecoder.decode(pair.substring(0, i), "UTF-8"), URLDecoder.decode(pair.substring(i + 1), "UTF-8"));
        }
        return queryPairs;
    }
    public Controller(){
        this.data = null;
    }
    public Controller(HashMap<String, Object> data){
        this.data = data;
    }
    private HashMap<String, String> parseParams(URI httpUrl){
        String url = httpUrl.getPath();
        HashMap<String, String> params = new HashMap<>();
        for(Server.Param p : this.rules){
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
        this.params = parseParams(he.getRequestURI());
        try{
            this.query = parseQuery(he.getRequestURI());    
        }catch(UnsupportedEncodingException ue){
            System.out.println("Error parsing query: " + ue);   
        }
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