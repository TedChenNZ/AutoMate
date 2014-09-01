package com.automates.automate.routines;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;
import android.util.Log;

import com.automates.automate.PhoneService;

public class TimelyChecker extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Time time = new Time();
		time.setToNow();
		Log.d("TimelyChecker", "Time is " + time.hour + ":" + time.minute + ", day is " + time.weekDay);
		//PhoneState.getRoutineApplier().checkRoutines();
		PhoneService.getInstance().update(context);
		RoutineApplier rA = PhoneService.getInstance().getRoutineApplier();
		if (rA != null) {
			rA.checkRoutines();
		} else {
			Log.d("TimelyChecker", "rA is null");
		}
	
    }

}
