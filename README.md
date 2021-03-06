# Advance-Java
A lightweight REST framework for Java, built on top of com.sun.net.httpserver
## Getting Started
Read the following to set up Advance-Java on your machine
### Prerequisites
* [Java Development Kit](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Freemarker](https://freemarker.apache.org/docs/pgui_quickstart_all.html) (if using source code)
* Java Servlet API
* Apache Commons File Upload
### Installation
Your Java project should look like this:
```java
src->
    main->
        java->
            project_name
```
#### Install from JAR file (recommended)
Installing from a JAR file depends on your build manager, so see their documentation. However, you will generally [download the JAR file](https://github.com/isaackrementsov/advance-java/blob/master/build/libs/advance-java.JAR) and put it into a `libs` folder and then compile it to your classpath (where binary files are stored). Installation from a JAR file does not require external dependencies and makes it easy to update the framework.
#### Install from source code
Simply download the Github code and copy the `advance` folder into your `java` folder. Also, make sure that the specified dependencies are installed.
#### Import package
To use the framework, import it like so:
```java
import advance.*;
//OR
import advance.Server;
import advance.Controller;
```
## Build Your First App
### Setting up the server
First, you need to build a server that directs HTTP traffic to Controllers, so create a file named `App.java` and put it into your project directory.
```java
package project_name;
import advance.Server;
public class App {
    public static void main(String[] args){
        //Initialize a server with a specified port
        Server app = new Server(3000, "/your/project/root/directory");
        //Listen on port 3000
        app.listen();
    }
}
```
### Adding controllers
Right now, your server will do nothing. To route URLs to actions, create a controller in the file `/controllers/MainController.java`.
```java
package project_name.controllers;
import advance.Controller;
public class MainController extends Controller {
    //Create a receiver for the GET method
    public void get(){
        //Set a byte[] response to be sent
        super.response = "Hello world!".getBytes();
    }
}
```
Now, edit your `App.java` file to look like this:
```java
package project_name;
import advance.Server;
import project_name.controllers;
public class App {
    public static void main(String[] args){
        Server app = new Server(3000, "/root");
        //Route any "/" request to MainController
        app.addController("/", new MainController());
        app.listen();    
    }
}
```
You now have an app that will send `hello world` to the browser at `localhost:3000` ! Continue reading for further information.
## Routing
### Static file routing
If you want to automatically return static files within a public directory, use the `Server` `addStaticController` method in your app's main method. If a nonexistent file is requested, the method will return a 404 error.
```java
//Your url should be a path that the root directory you set leads to.
app.addStaticController("/url/");
```
### REST Methods
Advance supports all of the HTTP methods (GET, POST, PUT, PATCH, DELETE). You can handle one of them by creating a controller method with a corresponding lowercase name. If not handler is found, the server will respond witha 405 status code. Here is how an app would handle a couple of methods:
`App.java`
```java
package project_name;
import advance.Server;
import project_name.controllers;
public class App {
    public static void main(String[] args){
        Server app = new Server(3000, "/root");
        app.addController("/showMethod", new MethodsController());
        app.listen();    
    }
}
```
`MethodsController.java`
```java
package project_name.controllers;
import advance.Controller;
public class MethodsController extends Controller {
    public void get(){
        super.response = "GET request to /showMethod".getBytes();
    }
    public void post(){
        super.response = "POST request to /showMethod".getBytes();
    }
    public void patch(){
        super.response = "PATCH request to /showMethod".getBytes();
    }
    public void put(){
        super.response = "PUT request to /showMethod".getBytes();
    }
    public void delete(){
        super.response = "DELETE request to /showMethod".getBytes();
    }
}
```
### Parameters and queries
Advance `Controller`s will automatically parse URL parameters (if specified) and any query strings that are detected. You can set url parameters by passing a url like `/your/url/:param` into the `Server` `addController` method. Queries and parameters are found in the `super.query` and `super.params` hashmaps, both of which are accessible to controller methods. Below is an example.
`App.java`
```java
package project_name;
import advance.Server;
import project_name.controllers;
public class App {
    public static void main(String[] args){
        Server app = new Server(3000, "/root");
        app.addController("/search/:category", new ParamController());
        app.listen();    
    }
}
```

`ParamController.java`
```java
package project_name.controllers;
import advance.Controller;
public class ParamController extends Controller {
    public void get(){
        //If request url is /search/laptops?price=500
        String resp = "Search: " + super.params.get("category") + ", $" + super.query.get("price");
        super.response = resp.getBytes(); //Browser will show "Search: Laptop, $500";
    }
}
```
## Sessions
Sessions are easy to use and automatically setup in Advance as hashmaps. Access them in your app as follows:
`SessionController.java` - can be any controller
```java
package project_name.controllers;
import advance.Controller;
import project_name.db.*;
public class SessionController extends Controller {
    public void get(){
        super.session.put("username", "johnsmith")
        super.session.put("password", "password123");
        super.response = "Session set".getBytes();
    }
    public void post(){
        User newUser = new User(super.session.get("SID"), super.session.get("username"), super.session.get("password"));
        newUser.save();
        super.redirect("/user/home", 302);
    }
}
```
### Delete session data
Deleting session data is important to change the state of the current user or log the user out. Specific keys can be deleted as in any HashMap (`super.session.put("mykey", null)`); however DO NOT delete the `SID` session field, as it will result in a `NullPointerException`. Deleting all session data aside from an SID is done by calling `super.deleteSession()` within a controller method.
### Plug in a custom session engine
`app.java`
```java
package project_name;
import advance.Server;
import project_name.controllers;
public class App {
    public static void main(String[] args){
        Server app = new Server(3000, "/root");
        Server.sessionStore = new MySessionStore();
        //Sessions can still be used normally
        app.addController("/search/:category", new ParamController());
        app.listen();    
    }
}
```
`MySessionStore.java`
```java
package project_name;
import advance.SessionStore;
import java.util.HashMap;
public class MySessionStore implements SessionStore {
    public HashMap<String, Object> get(String key){/*method implementation*/}
    public void set(String key, HashMap<String, Object> value){/*method implementation*/}
}
```
Any session store that you plugin must have the above methods to implement the `SessionStore` interface and be properly used as a storage engine.
## File uploads
Uploading files in Advance is fairly simple (credit for algorithm goes to [apimeister.com](https://apimeister.com/2015/10/10/formdatahandler-implements-com-sun-net-httpserver-httphandler.html)). On the client side, you should have a form that looks like this:
```html
<form method="post" action="/form/url" enctype="multipart/form-data">
    <input type="hidden" name="file" value="upl">
    <input type="file" name="myfileinput">
    <input type="submit" value="Submit">
</form>
```
On the server side, any form with `enctype="multipart/form-data"` will automatically be parsed into usable data using the `Controller.MultiPart` class.
```java
public static class MultiPart {
        public PartType type;
        public String contentType;
        public String name;
        public String filename;
        public String value;
        public byte[] bytes;
        public void save() throws IOException {
            FileUtils.writeByteArrayToFile(new File(filename), bytes);
        }
    }
```
The files are stored as a `MultiPart[]` in the `super.files` variable, where they can be accessed and manipulated in controller methods.
## Rendering Templates
Advance comes with the templating engine [Apache Freemarker](https://freemarker.apache.org/docs/pgui_quickstart_all.html), which is a simple but full-featured engine that can embed server-side code into HTML. If you install Advance using source code, you will also need to install freemarker so that it is usable as a dependency to Advance.
### Setup
You will need to register some basic information to make rendering simpler from your controllers.
`App.java`
```java
package project_name;
import advance.Server;
import project_name.controllers;
public class App {
    public static void main(String[] args){
        //Views should be stored at /path/to/project/root/your_view_directory/
        Server app = new Server(3000, "/path/to/project/root");
        app.setViewDir("/your_view_directory/");
        app.addController("/", new RenderController());
        app.listen();    
    }
}

```
### Rendering from controllers
The `Controller` `render` method takes a filename pointing to a template and an object (normally a hashmap) to supply information inside of a controller method.
```java
HashMap<String, Object> data = new HashMap<>();
data.put("info", "stuff");
data.put("otherinfo", "more stuff");
//Full template name should be "filename.ftlh"
super.render("filename", data);
//Access values in the template like <h1>${info}<h1> --> <h1>stuff</h1>. See Freemarker docs for more information.
```
## HTTP Forms
### GET forms
HTTP GET forms are simply url queries, so they are already available in the `super.query` object.
### POST, PUT, PATCH and DELETE forms
Other form methods can be accessed within controller methods as follows:
```java
String data = super.body.get("input-name");
```
#### PUT, PATH, and DELETE forms
Because HTML forms do not support HTTP verbs other than GET and POST, you will need to add the following hidden input to a POST form:
```html
<input type="hidden" name="_method" method="MYMETHOD">
```
This will direct HTTP traffic to the specified method and its corresponding controller function. You can also add a url query like so: `/form/url?_method=MYMETHOD`.
## Other HTTP Nuts & Bolts
### Setting an HTTP response
This was glanced over previously, but this is the code to add in a controller:
```java
super.response = "Your response here".getBytes();
```
### Changing the response code
The response code is set to 200 by default, but, to change it, you can do the following in a controller method:
```java
super.responseCode = 404;
```
### Changing response headers
Changing response headers is simple; in a controller add this code:
```java
super.headerEdits.put("key", "value");
```
### Change content type
The HTTP `Content-Type` header is set to `text/html` by default. To change it, do this in a controller:
```java
super.contentType = "mime/type";
```
### Override standard processes
By default, Advance will set response headers, write a response, and close the response stream after your controller method executes. If you would like to prevent one of these processes from happening, simply let the `Controller` know and then add a custom list.
```java
//Prevent the sendResponseHeaders(code, length) method
super.overrideSendHeaders = true;
//Prevent the HeaderEdit map from being saved to headers and prevent the Content-Type header from being added
super.overrideHeaders = true;
//Prevent the Controller form automatically writing whatever response is set
super.overrideWrite = true;
//Prevent the Controller from closing the OutputStream that it uses to write responses
super.overrideClose = true;
/*
replacement functions
*/
```
### Redirects
Redirection is done by a function that takes the response code and url for redirection. 
```java
//A 302 redirect is what you will usually use when sending the user around a web app
super.redirect("/url/", 302);
```
### Get the basic HttpExchange
The HttpExchange object passed in by com.sun.net.httpserver is still available if you write
```java
super.rawExchange;
```
in a controller method.
## Running a project
Advance projects can be run like any other Java program. Access the server using `http://localhost:port-you-set` in the browser.