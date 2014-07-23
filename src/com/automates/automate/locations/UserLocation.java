package com.automates.automate.locations;

import android.location.Location;

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
	
	/**
	 * Check if a given location is within the radius of this UserLocation
	 * @param loc
	 * @return boolean
	 */
	public boolean containsLocation(LatLng loc) {
		if (location == null) {
			return false;
		}
		if (loc == null) {
			return false;
		}
		float[] results = new float[1];
		double centerLatitude = this.location.latitude;
		double centerLongitude = this.location.longitude;
		double testLatitude = loc.latitude;
		double testLongitude = loc.longitude;
		Location.distanceBetween(centerLatitude, centerLongitude, testLatitude, testLongitude, results);
		float distanceInMeters = results[0];
		boolean isWithinRadius = distanceInMeters < radius;
		
		return isWithinRadius;
	}
	
	public boolean containsLocation(Location loc) {
		LatLng l = new LatLng(loc.getLatitude(), loc.getLongitude());
		return this.containsLocation(l);
		
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
