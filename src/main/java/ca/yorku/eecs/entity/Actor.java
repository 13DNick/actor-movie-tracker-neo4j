package ca.yorku.eecs.entity;

import java.util.ArrayList;
import java.util.List;

public class Actor {
	
	private String id;
	private String name;
	private List<String> movies;
	private double averageRating;
	
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
	
	public double getAverageRating() {
		return this.averageRating;
	}
	
	public void setAverageRating(double averageRating) {
		this.averageRating = averageRating;
	}
	
	public List<String> getMovies() {
		return this.movies;
	}

	public void setMovies(List<String> movies) {
		this.movies = movies;
	}

	@Override
	public String toString() {
		if(this.getMovies() != null) {
			return "Actor [id=" + id + ", name=" + name + ", movies=" + movies + "]";
		}
		return "Actor [id=" + id + ", name=" + name + ", averageRating=" + averageRating + "]";
	}
	
	public String toJsonString() {
		String result = "";
		
		if(this.getMovies() != null) {
			result += "{";
			
			result += "\"";
			result += "actorId";
			result += "\": ";	
					
			result += "\"";
			result += this.id;
			result += "\", ";	
			
			result += "\"";
			result += "name";
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
		} else {
			result += "{";
			
			result += "\"";
			result += "actorId";
			result += "\": ";	
					
			result += "\"";
			result += this.id;
			result += "\", ";	
			
			result += "\"";
			result += "name";
			result += "\": ";	
					
			result += "\"";
			result += this.name;
			result += "\", ";	
			
			result += "\"";
			result += "averageRating";
			result += "\": ";	
					
			result += "\"";
			result += this.averageRating;
			result += "\"";	
			
			result += "}";
		}
		
		return result;
	}
}
