package ca.yorku.eecs;

import java.io.IOException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;

import ca.yorku.eecs.controller.RequestHandler;

public class App 
{
    static int PORT = 8080;
    public static void main(String[] args) throws IOException
    {
        //create server & server context, then start the server
    	HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", PORT), 0);
        
    	RequestHandler requestHandler = new RequestHandler();
    	
    	server.createContext("/api/v1", requestHandler::handle);
        
    	server.start();
        System.out.printf("Server started on port %d...\n", PORT);
    }
}
