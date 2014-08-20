package com.automates.automate;

import com.automates.automate.routines.Routine;

public final class Logger {
	
	private static Logger instance = null;
	private static String wifiBSSID;
	private static boolean mdata;
	private static Routine routine;
	
	private Logger() {
		// Exists only to defeat instantiation.
		// Set to private -> no subclassing
	}
	public static Logger getInstance() {
		if(instance == null) {
			instance = new Logger();
		}
		return instance;
	}
	
	public static void logConnectivity(String w, boolean d)
	{
		wifiBSSID = w;
		mdata = d;
	}
	public static void logRoutine(Routine r) {
		routine = r;
	}
	public static String getWifiBSSID() {
		return wifiBSSID;
	}
	public static boolean getMData() {
		return mdata;
	}
	public static Routine getRoutine() {
		return routine;
	}
}

