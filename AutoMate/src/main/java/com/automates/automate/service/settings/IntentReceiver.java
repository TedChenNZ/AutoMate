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

public class IntentReceiver extends BroadcastReceiver {	
	private final static String TAG = "IntentReceiver";
	private final static int TIME_FRAME = 10 * 1000; // 5 seconds
	@Override
	public void onReceive(Context context, Intent intent) {
		PhoneService.getInstance().update(context);
		String eventCategory = PhoneService.getInstance().getEventCategory(intent);
        if (eventCategory == Settings.BOOT) {
            eventCategory = null;
        }
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		boolean learning = sharedPrefs.getBoolean("pref_learning", true);
		
		if (eventCategory != null && learning) {
			String eventAction = PhoneService.getInstance().getEventAction(eventCategory);
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
	private boolean connectivityCheck(String event, String eventAction) {
		if(event.equalsIgnoreCase(Settings.MDATA) && eventAction.equalsIgnoreCase("true")){
			if(PhoneService.getInstance().getWifiBSSID().equals("true")){
				return true;
			}
		}
		return false;
	}

}
