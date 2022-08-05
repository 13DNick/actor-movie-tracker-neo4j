package ca.yorku.eecs.entity;

import java.util.ArrayList;
import java.util.List;

public class AllActorsWithAverageMovieRating {
private List<Actor> actorsWithAverageRating;
	
	public AllActorsWithAverageMovieRating() {
		
	}
	
	public AllActorsWithAverageMovieRating(List<Actor> actorsWithAverageRating) {
		this.actorsWithAverageRating = actorsWithAverageRating;
	}

	public List<Actor> getActorsWithAverageRating() {
		return actorsWithAverageRating;
	}

	public void setActorsWithAverageRating(List<Actor> actorsWithAverageRating) {
		this.actorsWithAverageRating = actorsWithAverageRating;
	}
	
	public void sortDesc() {
		ArrayList<Actor> tempList = new ArrayList<>(this.getActorsWithAverageRating());
		ArrayList<Actor> result = new ArrayList<>();
		
		while(!tempList.isEmpty()) {
			Actor highestRated = tempList.get(0);
			for(int i = 0; i < tempList.size(); i++) {
				if(tempList.get(i).getAverageRating() > highestRated.getAverageRating()) {
					highestRated = tempList.get(i);
				}
			}
			tempList.remove(highestRated);
			result.add(highestRated);
		}
		
		this.setActorsWithAverageRating(result);
	}

	@Override
	public String toString() {
		return "AllActorsWithAverageMovieRating [actorsWithAverageRating=" + this.actorsWithAverageRating + "]";
	}
	
	public String toJsonString() {
		String result = "";
		
		result += "{";
		
		result += "\"";
		result += "ratings";
		result += "\": [";	
		
		for(Actor a: this.getActorsWithAverageRating()) {
			result += "";
			result += a.toJsonString();
			result += ", ";
		}
		
		result += "]}";
		
		return result;
	}
}
