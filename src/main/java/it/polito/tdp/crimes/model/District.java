package it.polito.tdp.crimes.model;

import com.javadocmd.simplelatlng.LatLng;

public class District {
	
	private int id;
	private LatLng coords;
	
	public District(int id, LatLng coords) {
		super();
		this.id = id;
		this.coords = coords;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LatLng getCoords() {
		return coords;
	}

	public void setCoords(LatLng coords) {
		this.coords = coords;
	}
	
}
