package ca.yorku.eecs.entity;

public class BaconNumber {
	
	private int baconNumber;
	
	public BaconNumber() {
		
	}
	
	public BaconNumber(int baconNumber) {
		this.baconNumber = baconNumber;
	}

	public int getBaconNumber() {
		return baconNumber;
	}

	public void setBaconNumber(int baconNumber) {
		this.baconNumber = baconNumber;
	}

	@Override
	public String toString() {
		return "BaconNumber [baconNumber=" + baconNumber + "]";
	}
}
