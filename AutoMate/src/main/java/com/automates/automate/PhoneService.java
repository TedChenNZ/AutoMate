package com.automates.automate;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.automates.automate.locations.LocationTrackerService;
import com.automates.automate.locations.UserLocation;
import com.automates.automate.locations.UserLocationService;
import com.automates.automate.settings.Data;
import com.automates.automate.settings.Initializer;
import com.automates.automate.settings.RingerProfiles;
import com.automates.automate.settings.Settings;
import com.automates.automate.settings.Wifi;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

// Singleton
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


	
	public String checkConnectivityIntent() {
//		String wifi = Logger.getWifiBSSID();
		boolean mdata = LoggerService.getInstance().getMData();
		if (mdata != dataEnabled) {
			return Settings.MDATA;

		} else {
//			if (!wifi.equals(wifiBSSID)) {
//				return Settings.WIFI;
//			}
		}
		return null;
	}

	public String getEventCategory(Intent intent) {
		String event = "";
		if (intent.getAction().equals("android.media.RINGER_MODE_CHANGED")) {
			event = Settings.RINGER;
		} else if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			event = Settings.BOOT;
		} else if (intent.getAction().equals("android.net.wifi.WIFI_STATE_CHANGED")) {
			event = Settings.WIFI;
		} else if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
			event = checkConnectivityIntent();
		}
		Log.d(TAG, "Event: " + event);
		return event;
	}

	public String getEventAction(String event) {
		String action = "";
		if (event.equals(Settings.WIFI)) {
			action = String.valueOf(wifiBSSID);
		} else if (event.equals(Settings.MDATA)) {
			action = String.valueOf(dataEnabled);
		} else if (event.equals(Settings.RINGER)) {
			action = String.valueOf(soundProfile);
//			action = "Changed";
		}
		return action;
	}


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