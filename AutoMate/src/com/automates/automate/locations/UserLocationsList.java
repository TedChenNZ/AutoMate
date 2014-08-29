package com.automates.automate.locations;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.Location;

import com.automates.automate.sqlite.UserLocationsManager;
import com.automates.automate.sqlite.UserLocationsSQLiteDBManager;

public class UserLocationsList extends ArrayList<UserLocation> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8241065009438371175L;


	private UserLocationsManager locationsDB;
	private List<UserLocation> currentULList;


	public UserLocationsList(Context context) {
		locationsDB = new UserLocationsSQLiteDBManager(context);
		currentULList = new ArrayList<UserLocation>();
		super.addAll(locationsDB.getAll());
	}

	@Override
	public boolean add(UserLocation ul) {
		return super.add(locationsDB.add(ul));
	}

	@Override
	public boolean remove(Object o) {
		locationsDB.remove((UserLocation) o);
		return super.remove(o);
	}

	@Override
	public UserLocation set(int index, UserLocation ul) {
		locationsDB.update(ul);
		return super.set(index, ul);
	}

	public List<UserLocation> checkLocation(Location loc) {
		int size = super.size();
		currentULList.clear();

		for (int i = 0; i < size; i++) {
			if (this.get(i).withinRadius(loc)) {
				currentULList.add(this.get(i));
			}
		}

		return currentULList;
	}
	
	public UserLocation getUserLocationFromName(String location) {
        for (UserLocation ul: this) {
            if (ul.getName().equals(location)) {
                return ul;
            }
        }
        return null;
	}
	
	public UserLocation getUserLocationFromID(int id) {
        for (UserLocation ul: this) {
            if (ul.getId() == id) {
                return ul;
            }
        }
        return null;
	}



}
