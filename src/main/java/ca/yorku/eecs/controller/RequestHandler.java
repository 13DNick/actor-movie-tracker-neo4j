package ca.yorku.eecs.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import ca.yorku.eecs.Utils;
import ca.yorku.eecs.service.Service;

public class RequestHandler implements HttpHandler{
	//service layer for db communication and business logic
	private Service service;
	
	public RequestHandler() {
		//instantiate db connection in Service constructor
		this.service = new Service();
	}
	
	@Override
	public void handle(HttpExchange request) throws IOException {
		
		//delegate request types
		try {
            if (request.getRequestMethod().equals("GET")) {
                handleGet(request);
                System.out.println("get");
            } else
            	Utils.sendString(request, "Unimplemented method\n", 501);
        } catch (Exception e) {
        	e.printStackTrace();
        	Utils.sendString(request, "500 Internal Server Error\n", 500);
        }
		
	}
	
	//handle all get methods
	private void handleGet(HttpExchange request) throws IOException {
    	
		//get request info
        URI uri = request.getRequestURI();
        String requestBody = Utils.convert(request.getRequestBody());
        String path = uri.getPath();
        String response = "";
        
        //process path
        ArrayList<String> pathList = new ArrayList<>(Arrays.asList(path.split("/")));
        pathList.remove(0);
        //System.out.println("List: " + pathList);
        
        //process query
        //String query = uri.getQuery();
        //System.out.println("Query: " + query);
        //Map<String, String> queryMap = new HashMap<>(splitQuery(query));
        
        //delegate call based on path
        if(pathList.get(2).equals("getActor")){
        	
            JSONObject deserialized;
        	String id;
    		
        	try {
    			//convert requestBody to JSON string and extract actorId
    			deserialized = new JSONObject(requestBody);
    			id = deserialized.getString("actorId");
    			
    			//delegate call to service
    			response = this.service.getActor(id);
    			
    			//process and return response
    			sendResponse(request, response); 
    			
    		} catch (Exception e) {
    			//improperly formatted JSON - throw 400 bad request
    			Utils.sendString(request, "400 Bad Request\n", 400);
    		}
        	
        } else if(pathList.get(2).equals("getMovie")){
        	
            JSONObject deserialized;
        	String id;
    		
        	try {
    			//convert requestBody to JSON string and extract movieId
    			deserialized = new JSONObject(requestBody);
    			id = deserialized.getString("movieId");
    			
    			//delegate call to service
    			response = this.service.getMovie(id);
    			
    			//process and return response
    			sendResponse(request, response); 
    				
    		} catch (Exception e) {
    			//improperly formatted JSON - throw 400 bad request
    			Utils.sendString(request, "400 Bad Request\n", 400);
    		}
        	
        } else {
        	//invalid path - throw 400 bad request
        	Utils.sendString(request, "400 Bad Request\n", 400);
        }
    }
	
	//helper method
	//process and return response for getActor and getMovie requests
	private void sendResponse(HttpExchange request, String response) throws Exception {
		if(response == null) {
			//id not found - throw 404 not found
			Utils.sendString(request, "404 Not Found\n", 404);
		} else if(response.equals("-1")) {
				//internal server error - throw 500
				Utils.sendString(request, "500 Internal Server Error\n", 500);
			} else {
			//id exists - output response
	        Utils.sendString(request, response, 200);
		}
	}

}
