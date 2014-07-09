package com.automates.automate.locations;

import com.google.android.gms.maps.model.LatLng;

public class UserLocation {
	private String name;
	private LatLng location;
	private Float radius;
	
	public UserLocation(String name, LatLng location, Float radius) {
		this.name = name;
		this.location = location;
		this.radius = radius;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LatLng getLocation() {
		return location;
	}

	public void setLocation(LatLng location) {
		this.location = location;
	}

	public Float getRadius() {
		return radius;
	}

	public void setRadius(Float radius) {
		this.radius = radius;
	}
}
