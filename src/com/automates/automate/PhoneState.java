package com.automates.automate;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;

import com.automates.automate.locations.GPSTracker;
import com.automates.automate.routines.settings.*;

// Singleton
public final class PhoneState {
	private static final String TAG = "PhoneState";
	private static Location location;
	private static boolean dataEnabled;
	private static boolean wifiEnabled;
	private static int soundProfile;

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

		soundProfile = SoundProfiles.getMode(context);
		wifiEnabled = Wifi.getWifiEnabled(context);
		dataEnabled = Data.getDataEnabled(context);
		GPSTracker gps = new GPSTracker(context); 
		location = gps.getLocation();
	}
	
	public static String checkConnectivityIntent() {
		String s = Logger.getLastLine();
		if (!s.equals(null)) {
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
	
	public static String getTrigger(Intent intent) {
		String trigger = "";
		if (intent.getAction().equals("android.media.RINGER_MODE_CHANGED")) {
			trigger = "Ringer";
		} else if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			trigger = "Bootup";
		} else if (intent.getAction().equals("android.net.wifi.WIFI_STATE_CHANGED")) {
			trigger = "Wifi";
		} else if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
			trigger = checkConnectivityIntent();
			if (trigger == null) {
				return null;
			} else {
//				Log.d(TAG, trigger);
			}
		}
		Log.d(TAG, "Trigger: " + trigger);
		return trigger;
	}
	
	public static void logIntent(Intent intent) {
		String s = "";
		String trigger = getTrigger(intent);
		if (trigger != null) {

			Time now = new Time();
			now.setToNow();
			String time = now.format("%H%M%S");
			
			s = trigger + "|" + time + "|" + soundProfile + "|" + wifiEnabled + "|" + dataEnabled + "|" + location;
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

}
