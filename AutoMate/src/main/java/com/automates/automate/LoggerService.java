package com.automates.automate;

import android.util.SparseArray;

public final class LoggerService {
	
	private static LoggerService instance = null;
	private String wifiBSSID;
	private boolean mdata;
	private SparseArray<Long> appliedRoutines;
	
	private LoggerService() {
		// Exists only to defeat instantiation.
		// Set to private -> no subclassing
		appliedRoutines = new SparseArray<Long>();
	}
	public static LoggerService getInstance() {
		if(instance == null) {
			instance = new LoggerService();
			
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
	
	
	public SparseArray<Long> getAppliedRoutinesWithinTimeframe(long time, long timeframe) {
		SparseArray<Long> appliedRecently = new SparseArray<Long>();
		for (int i = 0; i < appliedRoutines.size(); i++) {
			
			if ((time - appliedRoutines.valueAt(i)) < timeframe) {
				appliedRecently.put(appliedRoutines.keyAt(i), appliedRoutines.valueAt(i));
			}
		}
		return appliedRecently;
	}
}

