package com.automates.automate;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Environment;
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
	
	public static void logIntent(Intent intent) {
		String s = "";
		String trigger = "";
		if (intent.getAction().equals("android.media.RINGER_MODE_CHANGED")) {
			trigger = "Ringer";
		} else if (intent.getAction().equals("android.net.wifi.WIFI_STATE_CHANGED")) {
			trigger = "Wifi";
		} else if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
			trigger = "Wifi or Data";
		}
		s = trigger + "|" + soundProfile + "|" + wifiEnabled + "|" + dataEnabled + "|" + location;
		
		if (isExternalStorageWritable()) {
			Log.d(TAG, s);
			appendLog(s);
		} else {
			Log.d(TAG, "external not writable");
		}
		
	}
	
	public static void logLocation() {
		String s = "Location" + "|" + soundProfile + "|" + wifiEnabled + "|" + dataEnabled + "|" + location;
		
		appendLog(s);
		
	}
	
	/* Checks if external storage is available for read and write */
	private static boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	private static void appendLog(String text)
	{  
	String root = Environment.getExternalStorageDirectory().toString();
	   File logFile = new File(root + "/log.file");
	   if (!logFile.exists())
	   {
	      try
	      {
	         logFile.createNewFile();
	      } 
	      catch (IOException e)
	      {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	      }
	   }
	   try
	   {
	      //BufferedWriter for performance, true to set append to file flag
	      BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true)); 
	      buf.append(text);
	      buf.newLine();
	      buf.close();
	   }
	   catch (IOException e)
	   {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	   }
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
	
}
