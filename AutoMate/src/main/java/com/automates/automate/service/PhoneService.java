package com.automates.automate.service;
import android.content.Context;
import android.location.Location;

import com.automates.automate.model.UserLocation;
import com.automates.automate.service.location.LocationTrackerService;
import com.automates.automate.service.model.UserLocationService;
import com.automates.automate.service.routine.LoggerService;
import com.automates.automate.service.settings.Data;
import com.automates.automate.service.settings.Initializer;
import com.automates.automate.service.settings.RingerProfiles;
import com.automates.automate.service.settings.Settings;
import com.automates.automate.service.settings.Wifi;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Service which tracks phone settings and initializes services
 */
public final class PhoneService {
	private final String TAG = "PhoneService";
	private Location location;
	private boolean dataEnabled;
	private boolean wifiEnabled;
	private int soundProfile;

	private Context phoneContext;

	private String wifiBSSID;

	private static PhoneService instance = null;
	private PhoneService() {
		// Exists only to defeat instantiation.
		// Set to private -> no subclassing
	}
	public static PhoneService getInstance() {
		if(instance == null) {
			instance = new PhoneService();
		}
		return instance;
	}


    /**
     * Update the saved state of the phone
     * @param context
     */
	public void update(Context context) {
		// Initialize variables if they are not already initialized
	    phoneContext = context;
        if (!Initializer.isInitialized()) {
            Initializer.init(context);
        }
		// Update variables
		LoggerService.getInstance().logConnectivity(wifiBSSID, dataEnabled);
		
		soundProfile = RingerProfiles.getMode(context);
		wifiEnabled = Wifi.getWifiEnabled(context);
		dataEnabled = Data.getDataEnabled(context);
//		wifiBSSID = Wifi.getWifiBSSID(context);
		wifiBSSID = String.valueOf(wifiEnabled);
		location = LocationTrackerService.getInstance().getLastLocation();
		
		// Notify listeners
		notifyListeners();
	}







    /**
     * Get the current UserLocation
     * @return
     */
	public String getSetLocation() {
		if (location == null) {
			return null;
		}
		List<UserLocation> currentList = UserLocationService.getInstance().checkLocation(location);
		if (!(currentList.isEmpty())) {
			String s = "";
			for (UserLocation ul: currentList) {
				s = s + ul.getId() + ",";
			}
			s = s.substring(0, s.length() - 1);
			return s;
		}
		return "-1";
	}

	// Getters
	public Location getLocation() {
		return location;
	}
	public boolean isDataEnabled() {
		return dataEnabled;
	}
	public boolean isWifiEnabled() {
		return wifiEnabled;
	}
	public int getSoundProfile() {
		return soundProfile;
	}
	public String getWifiBSSID() {
		return wifiBSSID;
	}
	
	
	/**
	 * Listeners
	 */
	
	private List<PropertyChangeListener> listeners = new ArrayList<PropertyChangeListener>();
	
	private void notifyListeners() {
		for (PropertyChangeListener name : listeners) {
			name.propertyChange(new PropertyChangeEvent(getInstance(), "Triggers", true, true));
		}
	}

	public void addChangeListener(PropertyChangeListener newListener) {
		listeners.add(newListener);
	}
	public Context getContext() {
	    return phoneContext;
	}
	
	
}