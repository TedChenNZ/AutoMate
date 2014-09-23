package com.automates.automate.services.locations;

import android.content.Context;
import android.location.Location;

import com.automates.automate.model.UserLocation;
import com.automates.automate.sqlite.UserLocationDB;
import com.automates.automate.sqlite.UserLocationsManager;

import java.util.ArrayList;
import java.util.List;

public class UserLocationService {
	/**
	 * 
	 */


	private static UserLocationsManager locationsDB;
	private static List<UserLocation> currentULList;
    private static List<UserLocation> userLocationList;

    private static UserLocationService instance = null;

	private UserLocationService() {
        currentULList = new ArrayList<UserLocation>();
	}

    public static UserLocationService getInstance() {
        if(instance == null) {
            instance = new UserLocationService();
        }
        return instance;
    }

    public static void init(Context context) {
        locationsDB = new UserLocationDB(context);
        userLocationList = new ArrayList<UserLocation>();
        userLocationList.addAll(locationsDB.getAll());
    }


	public boolean add(UserLocation ul) {
		return userLocationList.add(locationsDB.add(ul));
	}


	public boolean remove(Object o) {
		locationsDB.remove((UserLocation) o);
		return userLocationList.remove(o);
	}

	public UserLocation set(int index, UserLocation ul) {
		locationsDB.update(ul);
		return userLocationList.set(index, ul);
	}

	public List<UserLocation> checkLocation(Location loc) {
		int size = userLocationList.size();
		currentULList.clear();

		for (int i = 0; i < size; i++) {
			if (userLocationList.get(i).withinRadius(loc)) {
				currentULList.add(userLocationList.get(i));
			}
		}

		return currentULList;
	}
	
	public UserLocation getUserLocationFromName(String name) {
        for (UserLocation ul: userLocationList) {
            if (ul.getName().equals(name)) {
                return ul;
            }
        }
        return null;
	}
	
	public UserLocation getUserLocationFromID(int id) {
        for (UserLocation ul: userLocationList) {
            if (ul.getId() == id) {
                return ul;
            }
        }
        return null;
	}


    public List<UserLocation> getAllUserLocations() {
        return userLocationList;
    }
}
