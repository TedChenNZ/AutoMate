package com.automates.automate.locations;

import com.google.android.gms.maps.model.LatLng;

public class UserLocation {
	private String name;
	private LatLng location;
	private Double radius;
	private String locationName;
	
	public UserLocation() {
		locationName = "";
	}
	
	public UserLocation(String name, LatLng location, Double radius, String locationName) {
		this.name = name;
		this.location = location;
		this.radius = radius;
		this.locationName = locationName;
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

	public Double getRadius() {
		return radius;
	}

	public void setRadius(Double radius) {
		this.radius = radius;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
}
