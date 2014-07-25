package com.automates.automate.routines.settings;

import java.lang.reflect.Method;

import android.content.Context;
import android.net.ConnectivityManager;

public class Data {
	public Data() {}
	
	// From http://stackoverflow.com/questions/13171623/how-to-turn-on-3g-mobile-data-programmatically-in-android
	public static void setDataEnabled(Context context, boolean enabled) {
		ConnectivityManager dataManager;
		dataManager  = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		Method dataMtd;
		try {
			dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
			dataMtd.setAccessible(true);
			dataMtd.invoke(dataManager, enabled); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
	}
	
	// From http://stackoverflow.com/questions/12806709/android-how-to-tell-if-mobile-network-data-is-enabled-or-disabled-even-when
	public static boolean getDataEnabled(Context context) {
		boolean mobileDataEnabled = false; // Assume disabled
	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    try {
	        Class<?> cmClass = Class.forName(cm.getClass().getName());
	        Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
	        method.setAccessible(true); // Make the method callable
	        // get the setting for "mobile data"
	        mobileDataEnabled = (Boolean)method.invoke(cm);
	    } catch (Exception e) {
	        // Some problem accessible private API
	        // TODO do whatever error handling you want here
	    }
	    return mobileDataEnabled;
	}
}
