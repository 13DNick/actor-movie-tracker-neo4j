package ca.yorku.eecs.service;

import static org.neo4j.driver.v1.Values.parameters;

import org.json.JSONObject;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;

import ca.yorku.eecs.entity.Actor;
import ca.yorku.eecs.entity.Movie;

public class Service {
	
	private String uriDb;
	private Driver driver;
	
	//initialize connection via constructor
	public Service() {
		uriDb = "bolt://localhost:7687"; // may need to change if you used a different port for your DBMS
		Config config = Config.builder().withoutEncryption().build();
		driver = GraphDatabase.driver(uriDb, AuthTokens.basic("neo4j","123456"), config);
	}
	
	//find actor by id
	public String getActor(String id) {
		try (Session session = driver.session()){
			
			//begin transaction and run query
			Transaction tx = session.beginTransaction();
			StatementResult result = tx.run("MATCH (a: actor WHERE a.id=$id) RETURN a", parameters("id", id));
			
			String f = "";
			
			//return json string of actor if a result exists
			if(result.hasNext()) {
				Record record = result.next();
				//System.out.println(record);
				Actor actor = new Actor();
				actor.setId(record.get(0).get("id").asString());
				actor.setName(record.get(0).get("Name").asString());
				f = new JSONObject(actor).toString();
			} else {
				//actor id not found
				f = null;
			}
			
			session.close();
			return f;
		} catch (Exception e) {
			//throw server error
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
				if(result.hasNext()) {
					Record record = result.next();
					//System.out.println(record);
					Movie movie = new Movie();
					movie.setId(record.get(0).get("id").asString());
					movie.setName(record.get(0).get("Name").asString());
					f = new JSONObject(movie).toString();
				} else {
					//movie id not found
					f = null;
				}
				
				session.close();
				return f;
			} catch (Exception e) {
				//throw server error
				return "-1";
			}
		}
	
	
	
	public void close() {
		driver.close();
	}
	
}
