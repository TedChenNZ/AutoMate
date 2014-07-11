package com.automates.automate.locations;

import com.google.android.gms.maps.model.LatLng;
/**
 *  Object for user set locations.
 *  Also works as model for SQLite DB 
 * @author Ted
 *
 */
public class UserLocation {
	private int id = -1; // For Database Purposes
	private String name;
	private LatLng location;
	private Integer radius;
	private String locationName = "";
	
	public UserLocation() {
	}
	
	public UserLocation(String name, LatLng location, Integer radius, String locationName) {
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

	public Integer getRadius() {
		return radius;
	}

	public void setRadius(Integer radius) {
		this.radius = radius;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	
	@Override
	public String toString() {
		return "UserLocation [id=" + id + ", name=" + name + ", location=" + location + ", radius=" + radius + ", locationName=" + locationName +"]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
