package com.automates.automate.settings;

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
	boolean learning = sharedPrefs.getBoolean("pref_learning", true);

	if (event != null && learning) {
	    Log.d("IntentReceiver", "event: " + event);
	    String eventAction = PhoneState.getEventAction(event);
	    Log.d("IntentReceiver", "eventAction: " + eventAction);
	    if(!randomBug(event, eventAction)){
		PatternControl pg = new PatternController(event,eventAction);
		pg.updateDatabase();
	    }
	}

    }
    private boolean randomBug(String event, String eventAction) {
	if(event.equalsIgnoreCase(Settings.MDATA) && eventAction.equalsIgnoreCase("true")){
	    if(PhoneState.getWifiBSSID().equals("true")){
		return true;
	    }
	}
	return false;
    }

}
