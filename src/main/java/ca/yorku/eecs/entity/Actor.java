package ca.yorku.eecs.entity;

import java.util.ArrayList;
import java.util.List;

public class Actor {
	
	private String id;
	private String name;
	private List<String> movies;
	
	public Actor() {
		
	}
	
	public Actor(String id, String name) {
		this.id = id;
		this.name = name;
		this.movies = new ArrayList<>();
	}
	
	public Actor(String id, String name, List<String> movies) {
		this.id = id;
		this.name = name;
		this.movies = movies;
	}
	
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public List<String> getMovies() {
		return this.movies;
	}

	public void setMovies(List<String> movies) {
		this.movies = movies;
	}

	@Override
	public String toString() {
		return "Actor [id=" + id + ", name=" + name + ", movies=" + movies + "]";
	}
	
	public String toJsonString() {
		String result = "";
		
		result += "{";
		
		result += "\"";
		result += "actorId";
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
		result += "movies";
		result += "\": ";	
				
		result += "";
		result += this.movies;
		result += "";	
		
		result += "}";
		
		return result;
	}
}
