package com.automates.automate.routines.settings;

import com.automates.automate.PhoneState;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.automates.automate.data.Pattern;
import com.automates.automate.data.PatternGenerator;

public class IntentReceiver extends BroadcastReceiver {	
	private final static String TAG = "IntentReceiver";
	@Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, intent.getAction());
        PhoneState.update(context);
        String event = PhoneState.getEvent(intent);
        if (event != null) {
	        String eventAction = PhoneState.getEventAction(event);
	        PatternGenerator pg = new PatternGenerator(event,eventAction);
	//        Pattern p = new Pattern(event, eventAction, PhoneState.getTime(), PhoneState.getSetLocation());
	        
	        PhoneState.logIntent(event);
        }

	}

}
