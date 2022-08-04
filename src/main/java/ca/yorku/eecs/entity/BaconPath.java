package ca.yorku.eecs.entity;

import java.util.List;

public class BaconPath {

	private List<String> baconPath;
	
	public BaconPath() {
		
	}
	
	public BaconPath(List<String> baconPath) {
		this.baconPath =  baconPath;
	}

	public List<String> getBaconPath() {
		return baconPath;
	}

	public void setBaconPath(List<String> baconPath) {
		this.baconPath = baconPath;
	}

	@Override
	public String toString() {
		return "BaconPath [baconPath=" + baconPath + "]";
	}
	
	public String toJsonString() {
		String result = "";
		
		result += "{";
		
		result += "\"";
		result += "baconPath";
		result += "\": ";	
				
		result += "";
		result += this.baconPath;
		result += "";	
		
		result += "}";
		
		return result;
	}
}
