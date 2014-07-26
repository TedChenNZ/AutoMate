package com.automates.automate.routines.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.automates.automate.PhoneState;
import com.automates.automate.pattern.PatternControl;
import com.automates.automate.pattern.PatternController;

public class IntentReceiver extends BroadcastReceiver {	
	private final static String TAG = "IntentReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, intent.getAction());
		PhoneState.update(context);
		String event = PhoneState.getEvent(intent);
		
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		boolean routines = sharedPrefs.getBoolean("pref_learning", true);
		
		if (event != null && routines) {
			Log.d("IntentReceiver", "event: " + event);
			String eventAction = PhoneState.getEventAction(event);
			PatternControl pg = new PatternController(event,eventAction);
			pg.updateDatabase();

			PhoneState.logIntent(event);
		}

	}

}