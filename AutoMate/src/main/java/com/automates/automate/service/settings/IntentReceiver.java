package com.automates.automate.service.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseArray;

import com.automates.automate.service.routine.LoggerService;
import com.automates.automate.service.PhoneService;
import com.automates.automate.service.pattern.PatternGenerator;
import com.automates.automate.model.Routine;
import com.automates.automate.service.model.RoutineService;

/**
 * Listener for any relevant changes to phone settings
 */
public class IntentReceiver extends BroadcastReceiver {	
	private final static String TAG = "IntentReceiver";
	private final static int TIME_FRAME = 10 * 1000; // 5 seconds

    /**
     * Perform actions when a setting is changed
     * @param context
     * @param intent
     */
	@Override
	public void onReceive(Context context, Intent intent) {
		PhoneService.getInstance().update(context);
		String eventCategory = getEventCategory(intent);
        if (eventCategory == Settings.BOOT) {
            eventCategory = null;
        }
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		boolean learning = sharedPrefs.getBoolean("pref_learning", true);
		
		if (eventCategory != null && learning) {
			String eventAction = getEventAction(eventCategory);
			// Stop pattern learning for routines made by the app
			if (checkIntentFromRoutine(eventCategory)) {
				return;
			}
			if(!connectivityCheck(eventCategory, eventAction)){
				PatternGenerator pg = new PatternGenerator(eventCategory,eventAction);
				pg.updateDatabase();
			}
		}

	}

    /**
     * Check if the setting change was performed by AutoMate
     * @param event
     * @return
     */
	private boolean checkIntentFromRoutine(String event) {
		SparseArray<Long> appliedRecently = LoggerService.getInstance().getAppliedRoutinesWithinTimeframe(System.currentTimeMillis(), TIME_FRAME);
		Log.d(TAG, "applied: " + LoggerService.getInstance().getAppliedRoutines());
		Log.d(TAG, "appliedRecently: " + appliedRecently);

		boolean fromRoutine = false;
		Routine r = null;
		for (int i = 0; i < appliedRecently.size(); i++) {
			r = RoutineService.getInstance().getRoutine(appliedRecently.keyAt(i));
			Log.d(TAG, "Routine: "+r);
			if (r != null) {
				if (r.getEventCategory().equals(event)) {
					fromRoutine = true;
					break;
				}
			}
		}
		
		
		Log.d(TAG, "fromRoutine: " + fromRoutine);
		return fromRoutine;
	}

    /**
     * Check if the connectivity change was due to Wifi or Mobile Data being turned on or off
     * @param event
     * @param eventAction
     * @return
     */
	private boolean connectivityCheck(String event, String eventAction) {
		if(event.equalsIgnoreCase(Settings.MDATA) && eventAction.equalsIgnoreCase("true")){
			if(PhoneService.getInstance().getWifiBSSID().equals("true")){
				return true;
			}
		}
		return false;
	}

    /**
     * Get the changed setting
     * @param intent
     * @return
     */
    public String getEventCategory(Intent intent) {
        String event = "";
        if (intent.getAction().equals("android.media.RINGER_MODE_CHANGED")) {
            event = Settings.RINGER;
        } else if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            event = Settings.BOOT;
        } else if (intent.getAction().equals("android.net.wifi.WIFI_STATE_CHANGED")) {
            event = Settings.WIFI;
        } else if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
            event = checkConnectivityIntent();
        }
        Log.d(TAG, "Event: " + event);
        return event;
    }

    /**
     * Get the setting change
     * @param event
     * @return
     */
    public String getEventAction(String event) {
        String action = "";
        if (event.equals(Settings.WIFI)) {
            action = String.valueOf(PhoneService.getInstance().getWifiBSSID());
        } else if (event.equals(Settings.MDATA)) {
            action = String.valueOf(PhoneService.getInstance().isDataEnabled());
        } else if (event.equals(Settings.RINGER)) {
            action = String.valueOf(PhoneService.getInstance().getSoundProfile());
//			action = "Changed";
        }
        return action;
    }

    /**
     * Check if the connectivity listener was from change in mobile data
     * @return
     */
    public String checkConnectivityIntent() {
        boolean mdata = LoggerService.getInstance().getMData();
        if (mdata != PhoneService.getInstance().isDataEnabled()) {
            return Settings.MDATA;

        } else {
//			if (!wifi.equals(wifiBSSID)) {
//				return Settings.WIFI;
//			}
        }
        return null;
    }

}
