package ca.yorku.eecs.entity;

import java.util.ArrayList;
import java.util.List;

public class Movie {
	
	private String id;
	private String name;
	private List<String> actors;
	
	public Movie() {
		
	}
	
	public Movie(String id, String name) {
		this.id = id;
		this.name = name;
		this.actors = new ArrayList<>();
	}
	
	public Movie(String id, String name, List<String> actors) {
		this.id = id;
		this.name = name;
		this.actors = actors;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<String> getActors(){
		return this.actors;
	}
	
	public void setActors(List<String> actors) {
		this.actors = actors;
	}

	@Override
	public String toString() {
		return "Movie [id=" + id + ", name=" + name + ", actors=" + actors + "]";
	}

	public String toJsonString() {
		String result = "";
		
		result += "{";
		
		result += "\"";
		result += "movieId";
		result += "\": ";	
				
		result += "\"";
		result += this.id;
		result += "\", ";	
		
		result += "\"";
		result += "Name";
		result += "\": ";	
				
		result += "\"";
		result += this.name;
		result += "\", ";	
		
		result += "\"";
		result += "actors";
		result += "\": ";	
				
		result += "";
		result += this.actors;
		result += "";	
		
		result += "}";
		
		return result;
	}
}
