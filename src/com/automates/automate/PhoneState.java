package com.automates.automate;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.automates.automate.locations.GPSTracker;
import com.automates.automate.routines.settings.*;

// Singleton
public final class PhoneState {
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
		
		s = s + soundProfile + "|" + wifiEnabled + "|" + dataEnabled + "|" + location;
		
	}
	
	public static void logLocation() {
		
	}
	
	
	public void appendLog(String text)
	{       
	   File logFile = new File("sdcard/log.file");
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
