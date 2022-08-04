package ca.yorku.eecs.service;

import static org.neo4j.driver.v1.Values.parameters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.driver.v1.types.Path;

import ca.yorku.eecs.entity.Actor;
import ca.yorku.eecs.entity.BaconNumber;
import ca.yorku.eecs.entity.BaconPath;
import ca.yorku.eecs.entity.Movie;

public class Service {
	
	private String uriDb;
	private Driver driver;
	
	//initialize connection via constructor
	public Service() {
		uriDb = "bolt://localhost:7687";
		Config config = Config.builder().withoutEncryption().build();
		driver = GraphDatabase.driver(uriDb, AuthTokens.basic("neo4j","123456"), config);
	}
	
	//compute bacon number
	public String computeBaconNumber(String actorId) {
		BaconNumber bn;
		
		//if actorId == nm0000102 aka this is Kevin Bacon
		//return bacon number 0
		if(actorId.equals("nm0000102")) {
			bn = new BaconNumber(0);
			JSONObject bnObj = new JSONObject(bn);
			return bnObj.toString();
		}
		
		
		try(Session session = driver.session()){
			
			Path path = this.getShortestPath(actorId, session);
			
			//actorId does not exist or path not found
			if(path == null) {
				return "404";
			}
			
			//count movie nodes to get bacon number
			int baconNumber = 0;
			for(Node node: path.nodes()) {
				if(node.hasLabel("movie")) {
					baconNumber++;
				}
			}
			
			//process bacon number into JSON string
			bn = new BaconNumber(baconNumber);
			JSONObject bnObj = new JSONObject(bn);
				
			session.close();
			return bnObj.toString();
		} catch(Exception e) {
			e.printStackTrace();
			return "-1";
		}
	}
	
	public String computeBaconPath(String actorId) throws JSONException {
		List<String> baconPath = new ArrayList<>();
		
			//if actorId == nm0000102 aka this is Kevin Bacon
			//return path consisting of just his actorId
			if(actorId.equals("nm0000102")) {
				baconPath.add("nm0000102");
				BaconPath bp = new BaconPath(baconPath);
				JSONObject bnObj = new JSONObject(bp.toJsonString());
				return bnObj.toString();
			}
			
			try(Session session = driver.session()){
				
				//compute path
				Path path = this.getShortestPath(actorId, session);
				
				//actorId does not exist or path not found
				if(path == null) {
					return "404";
				}
				
				//visit each node and add id to baconPath list
				for(Node node: path.nodes()) {
					baconPath.add(node.get("id").toString());
				}
				
				//order from actor given to Kevin Bacon
				Collections.reverse(baconPath);
				
				//process bacon path into JSON string
				BaconPath bp = new BaconPath(baconPath);
				JSONObject bnObj = new JSONObject(bp.toJsonString());
					
				session.close();
				return bnObj.toString();
			} catch(Exception e) {
				e.printStackTrace();
				return "-1";
			}
	}
	
	public Path getShortestPath(String actorId, Session session) throws Exception{
		session = driver.session();
		Transaction tx = session.beginTransaction();
		
		//throw 404 if actorId does not exist
		StatementResult result = tx.run("MATCH (a: actor WHERE a.id=$actorId) RETURN a", parameters("actorId", actorId));
		if(!doesExist(result)) {
			return null;
		}
		
		result = tx.run("MATCH path=shortestPath((b:actor {id:'nm0000102'})-[:ACTED_IN*]-(a:actor {id:$actorId}))" + 
				" RETURN path", parameters("actorId", actorId));
		
		//path not found - throw 404
		if(!result.hasNext()) {
			return null;
		}
		
		Record record = result.next();
		Path path = record.get(0).asPath();
		return path;				 
	}
	
	//add actor
	public String addActor(String actorId, String name) {
		try(Session session = driver.session()){
			Transaction tx = session.beginTransaction();
			
			//throw 400 if actorId already exists
			StatementResult result = tx.run("MATCH (a: actor WHERE a.id=$actorId) RETURN a", parameters("actorId", actorId));
			if(doesExist(result)) {
				return "400";
			}
			tx.close();
			
			//write to db
			session.writeTransaction(tx2 -> tx2.run("CREATE (a: actor {id: $actorId, Name: $name})", parameters("actorId", actorId, "name", name)));
			session.close();
			return "200";	
		} catch(Exception e) {
			//signal handler that error occurred
			return "-1";
		}
	}
	
	//add movie
	public String addMovie(String movieId, String name) {
		try(Session session = driver.session()){
			Transaction tx = session.beginTransaction();
			
			//throw 400 if movieId already exists
			StatementResult result = tx.run("MATCH (m: movie WHERE m.id=$movieId) RETURN m", parameters("movieId", movieId));
			if(doesExist(result)) {
				return "400";
			}
			tx.close();
			
			//write to db
			session.writeTransaction(tx2 -> tx2.run("CREATE (m: movie {id: $movieId, Name: $name})", parameters("movieId", movieId, "name", name)));
			session.close();
			return "200";	
		} catch(Exception e) {
			//signal handler that error occurred
			return "-1";
		}
	}
	
	//add relationship
	public String addRelationship(String movieId, String actorId) {
		
		//check if relationship already exists
		if(this.hasRelationship(movieId, actorId).equals("true")){
			//signal to throw 400
			return "400";
		} else if(this.hasRelationship(movieId, actorId).equals("not found")) {
			//signal to throw 404
			return "404";
		}
		
		//relationship doesn't exist - add it
		try(Session session = driver.session()){
			
			//write to db
			StatementResult result = session.writeTransaction(tx -> tx.run("MATCH (a: actor), (m: movie) WHERE a.id=$actorId AND m.id=$movieId" 
					+ " CREATE (a)-[r:ACTED_IN]->(m) RETURN type(r)", parameters("actorId", actorId, "movieId", movieId)));
			session.close();
			return "200";	
			
		} catch(Exception e) {
			//signal handler that error occurred
			return "-1";
		}
	}
	
	//return "true" if relationship found or "false" if not found
	//return "not found" if actor or movie not in db
	public String hasRelationship(String movieId, String actorId) {
		
		String result = "false";
		
		try(Session session = driver.session()){
			
			//make sure actor and movie exist, if not throw 404
			Transaction tx = session.beginTransaction();
			StatementResult actorResult = tx.run("MATCH (a: actor WHERE a.id=$actorId) RETURN a", parameters("actorId", actorId));
			if(!actorResult.hasNext()) {
				result = "not found";
				return result;
			}
			
			StatementResult movieResult = tx.run("MATCH (m: movie WHERE m.id=$movieId) RETURN m", parameters("movieId", movieId));
			if(!movieResult.hasNext()) {
				result = "not found";
				return result;
			}
			
			//does movie exist in actors relationships?
			//get all movies actor has acted in
			//return false if empty - actor has not been in any movies
			//set result to true if movieId is found in actors movies
			List<String> actorMovies = getMovies(actorId);
			if(actorMovies.size() < 1) {
				return result;
			}
			for(String movie: actorMovies) {
				if(movie.equals(movieId)){
					result = "true";
				} 
			}
			
			//movie was not present in actors ACTED_IN relationship
			//return false, since hasRelationship == true iff 
			//movieId exists in actor.movies AND actorId exists in movie.actors
			if(result.equals("false")) {
				return result;
			}
			
			//does actor exist in movies relationships?
			List<String> movieActors = getActors(movieId);
			if(movieActors.size() < 1) {
				result = "false";
				return result;
			}
			for(String actor: movieActors) {
				if(actor.equals(actorId)){
					result = "true";
				}
			}	
			session.close();
			return result;
		} catch(Exception e) {
			//signal handler that error occured
			return "-1";
		}
		
	}
	
	//find actor by id
	public String getActor(String id) {
		try (Session session = driver.session()){
			
			//begin transaction and run query
			Transaction tx = session.beginTransaction();
			StatementResult result = tx.run("MATCH (a: actor WHERE a.id=$id) RETURN a", parameters("id", id));
			
			String f = "";
			
			//return json string of actor if a result exists
			if(doesExist(result)) {
				Record record = result.next();
				
				//populate pojo with fetched data
				Actor actor = new Actor();
				actor.setId(record.get(0).get("id").asString());
				actor.setName(record.get(0).get("Name").asString());
				actor.setMovies(this.getMovies(id));
				
				//convert pojo to JSON string
				JSONObject jsonActor = new JSONObject(actor.toJsonString());
				f = jsonActor.toString();
			} else {
				//actor id not found
				f = null;
			}
			
			session.close();
			return f;
		} catch (Exception e) {
			//signal request handler error occured
			return "-1";
		}
	}
	
	//find movie by id
	public String getMovie(String id) {
		try (Session session = driver.session()){
			
			//begin transaction and run query
			Transaction tx = session.beginTransaction();
			StatementResult result = tx.run("MATCH (m: movie WHERE m.id=$id) RETURN m", parameters("id", id));
			
			String f = "";
			
			//return json string of actor if a result exists
			if(doesExist(result)) {
				Record record = result.next();
				
				//populate pojo with fetched data
				Movie movie = new Movie();
				movie.setId(record.get(0).get("id").asString());
				movie.setName(record.get(0).get("Name").asString());
				movie.setActors(this.getActors(id));				
				
				//convert pojo to JSON string
				JSONObject jsonMovie = new JSONObject(movie.toJsonString());
				f = jsonMovie.toString();
			} else {
				//movie id not found
				f = null;
			}
				
			session.close();
			return f;
		} catch (Exception e) {
			//signal request handler error occured
			return "-1";
		}
	}
	
	//get all movies the existing actor has acted in
	public ArrayList<String> getMovies(String actorId) {
		
		ArrayList<String> movies = new ArrayList<>();
		
		try(Session session = driver.session()){
			
			//query all relationships of actor
			Transaction tx = session.beginTransaction();
			StatementResult actorResult = tx.run("MATCH (a: actor WHERE a.id=$actorId)-[r]-(b) RETURN r, a, b", parameters("actorId", actorId));
			
			//if relationships exist add to list
			if(doesExist(actorResult)) {
				while(actorResult.hasNext()) {
					
					Record record = actorResult.next();
					movies.add(record.get(2).get("id").asString());
					
				}
				//return populated list
				return movies;
			}				
			//return empty list - relationships don't exist
			return movies;
		} catch(Exception e) {
			return movies;
		}
	}
		
	//get all actors that have acted in the existing movie
	public ArrayList<String> getActors(String movieId) {
		
		ArrayList<String> actors = new ArrayList<>();
		
		try(Session session = driver.session()){
			
			//query all relationships of actor
			Transaction tx = session.beginTransaction();
			StatementResult movieResult = tx.run("MATCH (m: movie WHERE m.id=$movieId)-[r]-(b) RETURN r, m, b", parameters("movieId", movieId));
			
			//if relationships exist add to list
			if(doesExist(movieResult)) {
				while(movieResult.hasNext()) {
					
					Record record = movieResult.next();
					actors.add(record.get(2).get("id").asString());
					
				}
				//return populated list
				return actors;
			}				
			//return empty list - relationships don't exist
			return actors;		
		} catch(Exception e) {
			return actors;
		}
	}
		
	//helper method to check if a given actor/movie exists
	public boolean doesExist(StatementResult result) {
		if(result.hasNext()) {
			return true;
		}
		return false;
	}	
}
