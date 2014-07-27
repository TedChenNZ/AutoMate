package com.automates.automate.routines;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.automates.automate.PhoneState;

public class TimelyChecker extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
	// TODO Auto-generated method stub
	Log.d("test", "in timely checker");
	PhoneState.getRoutineApplier().checkRoutines();
    }

}
