package com.automates.automate;

import android.util.SparseArray;

import com.automates.automate.routines.Routine;

public final class Logger {
	
	private static Logger instance = null;
	private String wifiBSSID;
	private boolean mdata;
	private SparseArray<Long> appliedRoutines;
	
	private Logger() {
		// Exists only to defeat instantiation.
		// Set to private -> no subclassing
		appliedRoutines = new SparseArray<Long>();
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
	
	public void logRoutine(int id) {
		appliedRoutines.put(id, System.currentTimeMillis());

	}
	
	
	
	public String getWifiBSSID() {
		return wifiBSSID;
	}
	public boolean getMData() {
		return mdata;
	}
	
	public SparseArray<Long> getAppliedRoutines() {
		return appliedRoutines;
	}
	
	public void setAppliedRoutines(SparseArray<Long> appliedRoutines) {
		this.appliedRoutines = appliedRoutines;
	}
	
	public SparseArray<Long> getAppliedRoutinesWithinTimeframe(long timeframe) {
		SparseArray<Long> appliedRecently = new SparseArray<Long>();
		for (int i = 0; i < appliedRoutines.size(); i++) {
			
			if ((System.currentTimeMillis() - appliedRoutines.valueAt(i)) < timeframe) {
				appliedRecently.put(i, appliedRoutines.get(i));
			}
		}
		return appliedRecently;
	}
}

