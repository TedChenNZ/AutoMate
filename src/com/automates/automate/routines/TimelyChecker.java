package com.automates.automate.routines;

import com.automates.automate.PhoneState;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TimelyChecker extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
	// TODO Auto-generated method stub
	PhoneState.getRoutineApplier();
    }

}
