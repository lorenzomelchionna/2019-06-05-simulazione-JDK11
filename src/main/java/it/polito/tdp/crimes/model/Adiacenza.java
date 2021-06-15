package it.polito.tdp.crimes.model;

public class Adiacenza {
	
	private District d;
	private Double dist;
	
	public Adiacenza(District d, Double dist) {
		super();
		this.d = d;
		this.dist = dist;
	}

	public District getD() {
		return d;
	}

	public void setD(District d) {
		this.d = d;
	}

	public Double getDist() {
		return dist;
	}

	public void setDist(Double dist) {
		this.dist = dist;
	}
	
}
