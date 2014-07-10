package com.automates.automate;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.automates.automate.locations.*;
import com.automates.automate.routines.settings.*;
import com.automates.automate.sqlite.SQLiteDBManager;

// Singleton
public final class PhoneState {
	private static final String TAG = "PhoneState";
	private static Location location;
	private static boolean dataEnabled;
	private static boolean wifiEnabled;
	private static int soundProfile;
	private static SQLiteDBManager db;
	private static int time = 0;
	private static List<UserLocation> locationList = new ArrayList<UserLocation>();

	private static GPSTracker gpsTracker;

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
		db = new SQLiteDBManager(context);
		soundProfile = SoundProfiles.getMode(context);
		wifiEnabled = Wifi.getWifiEnabled(context);
		dataEnabled = Data.getDataEnabled(context);
		if (gpsTracker == null) {
			gpsTracker = new GPSTracker(context); 
		}
		location = gpsTracker.getLocation();
		time = (int) System.currentTimeMillis();

	}

	public static String checkConnectivityIntent() {
		String s = Logger.getLastLine();
		if (s != null) {
			String[] split = s.split("\\|");
			if (split.length < 5) {
				Log.d(TAG,"length error");
			} else {
				String wifi = split[3];
				String data = split[4];
				if (!data.equals(String.valueOf(dataEnabled))) {
					// dataChange = true;
					return "Data";

				} else if (!wifi.equals(String.valueOf(wifiEnabled))) {
					// wifiChange = true;
					return "Wifi";
				}
			}
		}
		return null;
	}

	public static String getEvent(Intent intent) {
		String event = "";
		if (intent.getAction().equals("android.media.RINGER_MODE_CHANGED")) {
			event = "Ringer";
		} else if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			event = "Bootup";
		} else if (intent.getAction().equals("android.net.wifi.WIFI_STATE_CHANGED")) {
			event = "Wifi";
		} else if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
			event = checkConnectivityIntent();
			if (event == null) {
				return null;
			} else {
				//				Log.d(TAG, event);
			}
		}
		Log.d(TAG, "Event: " + event);
		return event;
	}

	public static String getEventAction(String event) {
		String action = "";
		if (event.equals("Wifi")) {
			action = String.valueOf(wifiEnabled);
		} else if (event.equals("Data")) {
			action = String.valueOf(dataEnabled);
		} else if (event.equals("Ringer")) {
			action = String.valueOf(soundProfile);
		}
		return action;
	}

	public static void logIntent(String event) {
		String s = "";
		if (event != null) {


			String t = "" + time;

			s = event + "|" + t + "|" + soundProfile + "|" + wifiEnabled + "|" + dataEnabled + "|" + location;
			Logger.appendLog(s);
		}
	}

	//	public static void logLocation() {
	//		Time now = new Time();
	//		now.setToNow();
	//		String time = now.format("%H%M%S");
	//		String s = "Location" + "|" + time + "|" +soundProfile + "|" + wifiEnabled + "|" + dataEnabled + "|" + location;
	//		Logger.appendLog(s);
	//	}


	public static String getSetLocation() {
		return location.toString();
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
	public static int getTime() {
		return time;
	}
	public static GPSTracker getGPSTracker() {
		return gpsTracker;
	}
	public static SQLiteDBManager getDb() {
		return db;
	}

	public static List<UserLocation> getLocationList() {
		return locationList;
	}

	public static void setLocationList(List<UserLocation> locationList) {
		PhoneState.locationList = locationList;
	}

}
