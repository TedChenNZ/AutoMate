package com.automates.automate.routines;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;
import android.util.Log;

import com.automates.automate.PhoneState;

public class TimelyChecker extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.d("TimelyChecker", "in timely checker");
		Time time = new Time();
		time.setToNow();
		Log.d("TimelyChecker", "Time is " + time.hour + ":" + time.minute + ", day is " + time.weekDay);
		//PhoneState.getRoutineApplier().checkRoutines();
		PhoneState.update(context);
		RoutineApplier rA = PhoneState.getRoutineApplier();
		if (rA != null) {
			rA.checkRoutines();
		} else {
			Log.d("TimelyChecker", "rA is null");
		}
	
    }

}
