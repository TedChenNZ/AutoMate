package com.automates.automate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.os.Environment;
import android.util.Log;

public final class Logger {
	private static final String TAG = "Logger";
	private static final String filename = Environment.getExternalStorageDirectory().toString() + "/.AutoMate/log.file";
	
	
	private static Logger instance = null;
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

	
	/* Checks if external storage is available for read and write */
	private static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}
	
	public static void appendLog(String text)
	{  
		if (isExternalStorageWritable()) {
			File logFile = new File(filename);
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
		} else {
			Log.d(TAG, "external not writable");
		}
	}
	
	public static String getLastLine() {
		if (isExternalStorageWritable()) {
			File logFile = new File(filename);
			if (!logFile.exists())
			{
				return null;
			}
			try
			{
				BufferedReader buf = new BufferedReader(new FileReader(logFile)); 
				
				String sCurrentLine;
				String lastLine = "";

			    while ((sCurrentLine = buf.readLine()) != null) 
			    {
			        lastLine = sCurrentLine;
			    }
			    
				buf.close();
				return lastLine;
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}

