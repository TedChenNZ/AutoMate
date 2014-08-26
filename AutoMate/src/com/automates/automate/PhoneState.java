package com.automates.automate;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.automates.automate.locations.GPSTracker;
import com.automates.automate.locations.UserLocation;
import com.automates.automate.locations.UserLocationsList;
import com.automates.automate.routines.RoutineApplier;
import com.automates.automate.routines.RoutineManager;
import com.automates.automate.routines.TimelyChecker;
import com.automates.automate.settings.*;
import com.automates.automate.sqlite.PatternDB;
import com.automates.automate.sqlite.RoutineDB;

// Singleton
public final class PhoneState {
	private static final String TAG = "PhoneState";
	private static Location location;
	private static boolean dataEnabled;
	private static boolean wifiEnabled;
	private static long time = 0;
	private static int soundProfile;
	private static Context phoneContext;
	private static PatternDB patternDB;
	private static RoutineDB routineDB;
	private static GPSTracker gpsTracker;
	private static RoutineApplier routineApplier;
	private static UserLocationsList locationsList;
	private static AlarmManager alarmManager;
	private static String wifiBSSID;
	private static RoutineManager routineManager;

	private static PhoneState instance = null;
	private PhoneState() {
		// Exists only to defeat instantiation.
		// Set to private -> no subclassing
	}
	public static PhoneState getInstance() {
		if(instance == null) {
			instance = new PhoneState();
		}
		return instance;
	}

	public static void update(Context context) {
		// Initialize variables if they are not already initialized
	    phoneContext = context;
		if (patternDB == null) {
			patternDB = new PatternDB(context);
		}
		if (routineDB == null) {
			routineDB = new RoutineDB(context);
		}
		if (locationsList == null) {
			locationsList = new UserLocationsList(context);
		}
		if (gpsTracker == null) {
			gpsTracker = new GPSTracker(context);
		}
		if (routineApplier == null) {
			routineApplier = new RoutineApplier(context);
			startRoutineChecking(context);
		}
		if (routineManager == null) {
			routineManager = new RoutineManager(routineDB);
		}
		
		
		// Update variables
		Logger.getInstance().logConnectivity(wifiBSSID, dataEnabled);
		
		soundProfile = RingerProfiles.getMode(context);
		wifiEnabled = Wifi.getWifiEnabled(context);
		dataEnabled = Data.getDataEnabled(context);
//		wifiBSSID = Wifi.getWifiBSSID(context);
		wifiBSSID = String.valueOf(wifiEnabled);
		time = System.currentTimeMillis();
		location = gpsTracker.getLastLocation();
		
		// Notify listeners
		notifyListeners();
	}

	private static void startRoutineChecking(Context context) {
	    AlarmManager alarmManager=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	    Intent intent = new Intent(context, TimelyChecker.class);
	    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
	    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),30000,pendingIntent);
	}
	
	public static String checkConnectivityIntent() {
//		String wifi = Logger.getWifiBSSID();
		boolean mdata = Logger.getInstance().getMData();
		if (mdata != dataEnabled) {
			return Settings.MDATA;

		} else {
//			if (!wifi.equals(wifiBSSID)) {
//				return Settings.WIFI;
//			}
		}
		return null;
	}

	public static String getEventCategory(Intent intent) {
		String event = "";
		if (intent.getAction().equals("android.media.RINGER_MODE_CHANGED")) {
			event = Settings.RINGER;
		} else if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			event = "Bootup";
			return null;
		} else if (intent.getAction().equals("android.net.wifi.WIFI_STATE_CHANGED")) {
			event = Settings.WIFI;
		} else if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
			event = checkConnectivityIntent();
		}
		Log.d(TAG, "Event: " + event);
		return event;
	}

	public static String getEventAction(String event) {
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


	public static String getSetLocation() {
		if (location == null) {
			return null;
		}
		List<UserLocation> currentList = ((UserLocationsList) locationsList).checkLocation(location);
		if (!(currentList.isEmpty())) {
			String s = "";
			for (UserLocation ul: currentList) {
				s = s + ul.getId() + ",";
			}
			s = s.substring(0, s.length() - 1);
			return s;
		}
		return "";
	}

	// Getters
	public static Location getLocation() {
		return location;
	}
	public static boolean isDataEnabled() {
		return dataEnabled;
	}
	public static boolean isWifiEnabled() {
		return wifiEnabled;
	}
	public static int getSoundProfile() {
		return soundProfile;
	}
	public static long getTime() {
		return time;
	}
	public static PatternDB getPatternDb() {
		return patternDB;
	}
	public static RoutineDB getRoutineDb() {
		return routineDB;
	}
	public static GPSTracker getGPSTracker() {
		return gpsTracker;
	}
	public static RoutineApplier getRoutineApplier() {
		return routineApplier;
	}
	public static AlarmManager getAlarmManager() {
		return alarmManager;
	}
	public static UserLocationsList getLocationsList() {
		return locationsList;
	}
	public static void setLocationsList(UserLocationsList locationsList) {
		PhoneState.locationsList = locationsList;
	}
	public static String getWifiBSSID() {
		return wifiBSSID;
	}
	public static RoutineManager getRoutineManager() {
		return routineManager;
	}
	
	
	/**
	 * Listeners
	 */
	
	private static List<PropertyChangeListener> listeners = new ArrayList<PropertyChangeListener>();
	
	private static void notifyListeners() {
		for (PropertyChangeListener name : listeners) {
			name.propertyChange(new PropertyChangeEvent(getInstance(), "Triggers", true, true));
		}
	}

	public void addChangeListener(PropertyChangeListener newListener) {
		listeners.add(newListener);
	}
	public static Context getContext() {
	    return phoneContext;
	}
	
	
}