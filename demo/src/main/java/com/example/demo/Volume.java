package com.example.demo;

public class Volume {

	double longAverage;
	double shortAverage;
	String decision;

	public Volume(double longAverage, double shortAverage, String decision) {
		super();
		this.longAverage = longAverage;
		this.shortAverage = shortAverage;
		this.decision = decision;
	}
	public String getDecision() {
		return decision;
	}
	public void setDecision(String decision) {
		this.decision = decision;
	}
	public double getLongAverage() {
		return longAverage;
	}
	public void setLongAverage(double longAverage) {
		this.longAverage = longAverage;
	}
	public double getShortAverage() {
		return shortAverage;
	}
	public void setShortAverage(double shortAverage) {
		this.shortAverage = shortAverage;
	}

}
