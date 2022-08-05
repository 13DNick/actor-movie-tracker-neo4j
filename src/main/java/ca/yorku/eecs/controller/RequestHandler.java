package ca.yorku.eecs.controller;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import ca.yorku.eecs.Utils;
import ca.yorku.eecs.entity.ActedIn;
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
            } else if(request.getRequestMethod().equals("PUT")){
            	handlePut(request);
            } else {
            	Utils.sendString(request, "Unimplemented method\n", 501);
            }
        } catch (Exception e) {
        	e.printStackTrace();
        	Utils.sendString(request, "500 Internal Server Error\n", 500);
        }
		
	}
	
	//handle all put methods
	private void handlePut(HttpExchange request) throws IOException{
		
		//get request info
        URI uri = request.getRequestURI();
        String requestBody = Utils.convert(request.getRequestBody());
        String path = uri.getPath();
        
        
        //process path
        ArrayList<String> pathList = new ArrayList<>(Arrays.asList(path.split("/")));
        pathList.remove(0);
        
      
        String response = "";
        JSONObject deserialized;
    	String movieId;
    	String actorId;
        String name;
        double rating;
    	
        //delegate call based on path
        if(pathList.get(2).equals("addActor")){
        	try {
    			//extract values from request body
    			deserialized = new JSONObject(requestBody);
    			actorId = deserialized.getString("actorId");
    			name = deserialized.getString("name");
    			
    			//delegate call to service
    			response = this.service.addActor(actorId, name);
    			
    			//send appropriate response
    			this.sendPutResponse(request, response);	
    			
    		} catch (JSONException e) {
    			//improperly formatted JSON - throw 400 bad request
    			Utils.sendString(request, "400 Bad Request\n", 400);
    		}	
        } else if(pathList.get(2).equals("addMovie")) {
        	try {
    			//extract values from request body
    			deserialized = new JSONObject(requestBody);
    			movieId = deserialized.getString("movieId");
    			name = deserialized.getString("name");
    			
    			//check if rating is passed in request body
    			try{
    				rating = deserialized.getDouble("rating");
    				rating = Utils.trimToTwoDecimals(rating);
    				
    				if(rating < 0.0 || rating > 5.0) {
    					//invalid rating passed - set it to negative to ignore
    					rating = -1.0;
    				}
    			} catch(JSONException e) {
    				//if no rating is passed - set it to negative
    				//movies with negative rating will be ignored 
    				//when running methods working on ratings
    				rating = -1.0;
    			}
    			
    			//delegate call to service
    			response = this.service.addMovie(movieId, name, rating);
    			
    			//send appropriate response
    			this.sendPutResponse(request, response);
    			
    		} catch (JSONException e) {
    			//improperly formatted JSON - throw 400 bad request
    			Utils.sendString(request, "400 Bad Request\n", 400);
    		}
        } else if(pathList.get(2).equals("addRelationship")) {
        	try {
        		//extract values from request body
    			deserialized = new JSONObject(requestBody);
    			movieId = deserialized.getString("movieId");
    			actorId = deserialized.getString("actorId");
    			
    			//delegate call to service
    			response = this.service.addRelationship(movieId, actorId);
        		
    			//send appropriate response
    			this.sendPutResponse(request, response);
    			
        	} catch(JSONException e) {
        		//improperly formatted JSON - throw 400 bad request
    			Utils.sendString(request, "400 Bad Request\n", 400);
        	}
        } else {
        	//invalid path - throw 400 bad request
        	Utils.sendString(request, "400 Bad Request\n", 400);
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
        
        
        JSONObject deserialized;
    	String movieId;
    	String actorId;
        
        //delegate call based on path
        if(pathList.get(2).equals("getActor")){
        	
        	try {
    			//convert requestBody to JSON string and extract actorId
    			deserialized = new JSONObject(requestBody);
    			actorId = deserialized.getString("actorId");
    			
    			//delegate call to service
    			response = this.service.getActor(actorId);
    			
    			//process and return response
    			sendGetResponse(request, response); 
    			
    		} catch (JSONException e) {
    			//improperly formatted JSON - throw 400 bad request
    			Utils.sendString(request, "400 Bad Request\n", 400);
    		}
        	
        } else if(pathList.get(2).equals("getMovie")){
        	
        	try {
    			//convert requestBody to JSON string and extract movieId
    			deserialized = new JSONObject(requestBody);
    			movieId = deserialized.getString("movieId");
    			
    			//delegate call to service
    			response = this.service.getMovie(movieId);
    			
    			//process and return response
    			sendGetResponse(request, response); 
    				
    		} catch (JSONException e) {
    			//improperly formatted JSON - throw 400 bad request
    			Utils.sendString(request, "400 Bad Request\n", 400);
    		}
        	
        } else if(pathList.get(2).equals("hasRelationship")) {
        	
        	try {
        		//convert requestBody to JSON string
        		//extract movieId and actorId
    			deserialized = new JSONObject(requestBody);
    			movieId = deserialized.getString("movieId");
    			actorId = deserialized.getString("actorId");
    			
    			//delegate call to service
    			String result = this.service.hasRelationship(movieId, actorId);
    			
    			//return 404 if actor or movie not in DB
    			if(result.equals("not found")) {
    				Utils.sendString(request, "404 Not Found\n", 404);
    			}
    			
    			//return 500 Server Error if crash in hasRelationship()
    			if(result.equals("-1")) {
    				Utils.sendString(request, "500 Internal Server Error\n", 500);
    			}
    			
    			//convert to JSON string
    			ActedIn hasRelationshipResult = new ActedIn(result, actorId, movieId);
    			response = new JSONObject(hasRelationshipResult).toString();
    			
    			//send response
    			Utils.sendString(request, response, 200);
        	} catch(JSONException e) {
        		//improperly formatted JSON - throw 400 bad request
    			Utils.sendString(request, "400 Bad Request\n", 400);
        	}
			
        } else if(pathList.get(2).equals("computeBaconNumber")){
        	try {
        		
        		//convert requestBody to JSON string
    			deserialized = new JSONObject(requestBody);
    			actorId = deserialized.getString("actorId");
        		
    			//delegate call to service
    			String result = this.service.computeBaconNumber(actorId);
    			
    			//return 404 if actor or movie not in db
    			if(result.equals("404")) {
    				Utils.sendString(request, "404 Not Found\n", 404);
    			} else if(result.equals("-1")) {
    				//return 500 Server Error if crash in computeBaconPath()
    				Utils.sendString(request, "500 Internal Server Error\n", 500);
    			} else {
        			//send response
    				response = result;
        			Utils.sendString(request, response, 200);
    			}
    			
        	} catch(JSONException e) {
        		//improperly formatted JSON - throw 400 bad request
    			Utils.sendString(request, "400 Bad Request\n", 400);
        	}
        } else if(pathList.get(2).equals("computeBaconPath")){
        	try {
        		
        		//convert requestBody to JSON string
    			deserialized = new JSONObject(requestBody);
    			actorId = deserialized.getString("actorId");
        		
    			//delegate call to service
    			String result = this.service.computeBaconPath(actorId);
    			
    			//return 404 if actor or movie not in db
    			if(result.equals("404")) {
    				Utils.sendString(request, "404 Not Found\n", 404);
    			} else if(result.equals("-1")) {
    				//return 500 Server Error if crash in computeBaconPath()
    				Utils.sendString(request, "500 Internal Server Error\n", 500);
    			} else {
        			//send response
    				response = result;
        			Utils.sendString(request, response, 200);
    			}
    			
        	} catch(JSONException e) {
        		//improperly formatted JSON - throw 400 bad request
    			Utils.sendString(request, "400 Bad Request\n", 400);
        	}
        } else if(pathList.get(2).equals("ratings")){
        	//endpoint: /api/v1/ratings
        	if(pathList.size() == 3) {
        		try {
    	        	//delegate call to service
    				String result = this.service.getMoviesWithRatings();
    				
    				//return 404 if actor or movie not in db
    				if(result.equals("404")) {
    					Utils.sendString(request, "404 Not Found\n", 404);
    				} else if(result.equals("-1")) {
    					//return 500 Server Error if crash in computeBaconPath()
    					Utils.sendString(request, "500 Internal Server Error\n", 500);
    				} else {
    	    			//send response
    					response = result;
    	    			Utils.sendString(request, response, 200);
    				}				
    	    	} catch(IOException e) {
    	    		//improperly formatted JSON - throw 400 bad request
    				Utils.sendString(request, "400 Bad Request\n", 400);
    	    	}
        	} else if(pathList.size() == 4){
        		//endpoint: /api/v1/ratings/{0-5}
        		try {
    	        	int range = Integer.parseInt(pathList.get(3));
        			
    	        	//if range out of bounds throw 404
    	        	if(range < 0 || range > 5) {
    	        		throw new IOException();
    	        	}
    	        	
        			//delegate call to service
    				String result = this.service.getMoviesWithRatingsInRange(range);
    				
    				//return 404 if actor or movie not in DB
    				if(result.equals("404")) {
    					Utils.sendString(request, "404 Not Found\n", 404);
    				} else if(result.equals("-1")) {
    					//return 500 Server Error if crash in computeBaconPath()
    					Utils.sendString(request, "500 Internal Server Error\n", 500);
    				} else {
    	    			//send response
    					response = result;
    	    			Utils.sendString(request, response, 200);
    				}				
    	    	} catch(IOException e) {
    	    		//improperly formatted JSON - throw 400 bad request
    				Utils.sendString(request, "400 Bad Request\n", 400);
    	    	}
        	}
        	      	
        } else if(pathList.get(2).equals("actorsByMovieRatings")){
        	
        	try {
	        	//delegate call to service
				String result = this.service.getActorsByAverageMovieRatings();
				
				//return 404 if actor or movie not in DB
				if(result.equals("404")) {
					Utils.sendString(request, "404 Not Found\n", 404);
				} else if(result.equals("-1")) {
					//return 500 Server Error if crash in computeBaconPath()
					Utils.sendString(request, "500 Internal Server Error\n", 500);
				} else {
	    			//send response
					response = result;
	    			Utils.sendString(request, response, 200);
				}				
	    	} catch(IOException e) {
	    		//improperly formatted JSON - throw 400 bad request
				Utils.sendString(request, "400 Bad Request\n", 400);
	    	}
        	
        } else {
        	//invalid path - throw 400 bad request
        	Utils.sendString(request, "400 Bad Request\n", 400);
        }
    }
	
	//helper method
	//process and return response for all PUT requests
	private void sendPutResponse(HttpExchange request, String response) throws IOException{
		if(response.equals("200")) {
			Utils.sendString(request, "", 200);
		} else if(response.equals("-1")) {
			Utils.sendString(request, "", 500);
		} else if(response.equals("400")) {
			Utils.sendString(request, "", 400);
		} else if(response.equals("404")) {
			Utils.sendString(request, "", 404);
		}
	}
	
	//helper method
	//process and return response for getActor and getMovie requests
	private void sendGetResponse(HttpExchange request, String response) throws IOException {
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
