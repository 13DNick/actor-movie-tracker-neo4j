package ca.yorku.eecs.entity;

import java.util.List;

public class AllMovieRatings {
	
	private List<Movie> moviesWithRatings;
	
	public AllMovieRatings() {
		
	}
	
	public AllMovieRatings(List<Movie> moviesWithRatings) {
		this.moviesWithRatings = moviesWithRatings;
	}

	public List<Movie> getMoviesWithRatings() {
		return moviesWithRatings;
	}

	public void setMoviesWithRatings(List<Movie> moviesWithRatings) {
		this.moviesWithRatings = moviesWithRatings;
	}

	@Override
	public String toString() {
		return "AllMovieRatings [moviesWithRatings=" + moviesWithRatings + "]";
	}
	
	public String toJsonString() {
		String result = "";
		
		result += "{";
		
		result += "\"";
		result += "ratings";
		result += "\": [";	
		
		for(Movie m: this.getMoviesWithRatings()) {
			result += "";
			result += m.toJsonString();
			result += ", ";
		}
		
		result += "]}";
		
		return result;
	}
	
}
