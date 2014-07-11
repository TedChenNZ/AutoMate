package com.automates.automate.locations;

import java.util.ArrayList;

import android.content.Context;

import com.automates.automate.sqlite.UserLocationsSQLiteDBManager;

public class UserLocationsList extends ArrayList<UserLocation> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8241065009438371175L;
	private UserLocationsSQLiteDBManager locationsDB;
	
	public UserLocationsList(Context context) {
		locationsDB = new UserLocationsSQLiteDBManager(context);
		super.addAll(locationsDB.getAll());
	}

	@Override
	public boolean add(UserLocation ul) {
		locationsDB.add(ul);
		return super.add(ul);
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
}
