package com.automates.automate;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.automates.automate.locations.GPSTracker;
import com.automates.automate.locations.UserLocation;
import com.automates.automate.locations.UserLocationService;
import com.automates.automate.routines.RoutineApplier;
import com.automates.automate.routines.RoutineService;
import com.automates.automate.routines.TimelyChecker;
import com.automates.automate.settings.Data;
import com.automates.automate.settings.RingerProfiles;
import com.automates.automate.settings.Settings;
import com.automates.automate.settings.Wifi;
import com.automates.automate.sqlite.PatternDB;

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


	private PatternDB patternDB;
	private GPSTracker gpsTracker;
	private RoutineApplier routineApplier;
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

    // Initialise services
    private void init(Context context) {
        if (RoutineService.getInstance().getAllRoutines() == null) {
            RoutineService.getInstance().init(context);
        }

        if (UserLocationService.getInstance().getAllUserLocations() == null) {
            UserLocationService.getInstance().init(context);
        }

    }

	public void update(Context context) {
		// Initialize variables if they are not already initialized
	    phoneContext = context;
        init(context);
		if (patternDB == null) {
			patternDB = new PatternDB(context);
		}


		if (gpsTracker == null) {
			gpsTracker = new GPSTracker(context);
		}
		if (routineApplier == null) {
			routineApplier = new RoutineApplier(context);
			startRoutineChecking(context);
		}

		
		
		// Update variables
		Logger.getInstance().logConnectivity(wifiBSSID, dataEnabled);
		
		soundProfile = RingerProfiles.getMode(context);
		wifiEnabled = Wifi.getWifiEnabled(context);
		dataEnabled = Data.getDataEnabled(context);
//		wifiBSSID = Wifi.getWifiBSSID(context);
		wifiBSSID = String.valueOf(wifiEnabled);
		location = gpsTracker.getLastLocation();
		
		// Notify listeners
		notifyListeners();
	}

	private void startRoutineChecking(Context context) {
	    AlarmManager alarmManager=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	    Intent intent = new Intent(context, TimelyChecker.class);
	    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
	    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),30000,pendingIntent);
	}
	
	public String checkConnectivityIntent() {
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

	public String getEventCategory(Intent intent) {
		String event = "";
		if (intent.getAction().equals("android.media.RINGER_MODE_CHANGED")) {
			event = Settings.RINGER;
		} else if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			event = "BootUp";
            return null;
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
		return "";
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
	public PatternDB getPatternDb() {
		return patternDB;
	}
	public GPSTracker getGPSTracker() {
		return gpsTracker;
	}
	public RoutineApplier getRoutineApplier() {
		return routineApplier;
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