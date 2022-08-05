package ca.yorku.eecs.entity;

public class ActedIn {
	
	private boolean hasRelationship;
	private String actorId;
	private String movieId;
	
	public ActedIn(boolean hasRelationship, String actorId, String movieId) {
		this.hasRelationship = hasRelationship;
		this.actorId = actorId;
		this.movieId = movieId;
	}

	public boolean getHasRelationship() {
		return hasRelationship;
	}


	public void setHasRelationship(boolean hasRelationship) {
		this.hasRelationship = hasRelationship;
	}



	public String getActorId() {
		return actorId;
	}



	public void setActorId(String actorId) {
		this.actorId = actorId;
	}



	public String getMovieId() {
		return movieId;
	}



	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}

	@Override
	public String toString() {
		return "ActedIn [hasRelationship=" + hasRelationship + ", actorId=" + actorId + ", movieId=" + movieId + "]";
	}
	
}
