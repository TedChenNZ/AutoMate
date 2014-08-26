package com.automates.automate.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseArray;

import com.automates.automate.Logger;
import com.automates.automate.PhoneState;
import com.automates.automate.pattern.PatternControl;
import com.automates.automate.pattern.PatternController;
import com.automates.automate.routines.Routine;

public class IntentReceiver extends BroadcastReceiver {	
	private final static String TAG = "IntentReceiver";
	private final static int TIME_FRAME = 5 * 1000; // 5 seconds
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, intent.getAction());
		PhoneState.update(context);
		String eventCategory = PhoneState.getEventCategory(intent);

		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		boolean learning = sharedPrefs.getBoolean("pref_learning", true);
		
		if (eventCategory != null && learning) {
			String eventAction = PhoneState.getEventAction(eventCategory);
			// Stop pattern learning for routines made by the app
			if (checkIntentFromRoutine(eventCategory)) {
				return;
			}
			if(!connectivityCheck(eventCategory, eventAction)){
				PatternControl pg = new PatternController(eventCategory,eventAction);
				pg.updateDatabase();
			}
		}

	}
	
	private boolean checkIntentFromRoutine(String event) {
		SparseArray<Long> appliedRecently = Logger.getInstance().getAppliedRoutinesWithinTimeframe(TIME_FRAME);
		boolean fromRoutine = false;
		Routine r = null;
		for (int i = 0; i < appliedRecently.size(); i++) {
			r = PhoneState.getRoutineManager().getRoutine(appliedRecently.keyAt(i));
			
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
			if(PhoneState.getWifiBSSID().equals("true")){
				return true;
			}
		}
		return false;
	}

}
