package com.automates.automate;

import com.automates.automate.routines.Routine;

public final class Logger {
	
	private static Logger instance = null;
	private String wifiBSSID;
	private boolean mdata;
	private Routine routine;
	
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
	
	public void logConnectivity(String w, boolean d)
	{
		wifiBSSID = w;
		mdata = d;
	}
	public void logRoutine(Routine r) {
		routine = r;
	}
	public String getWifiBSSID() {
		return wifiBSSID;
	}
	public boolean getMData() {
		return mdata;
	}
	public Routine getRoutine() {
		return routine;
	}
}

