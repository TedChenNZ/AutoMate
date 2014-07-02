package com.automates.automate.routines.settings;

import com.automates.automate.PhoneState;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class IntentReceiver extends BroadcastReceiver {	
	private final static String TAG = "IntentReceiver";
	@Override
    public void onReceive(Context context, Intent intent) {
//		boolean isWifiConnected = false;
//        boolean isMobileConnected = false;
//        int soundProfile = 0;
//        isWifiConnected = Wifi.getWifiEnabled(context);
//        isMobileConnected = Data.getDataEnabled(context);
//        soundProfile = SoundProfiles.getMode(context);
//        Log.d("IntentReceiver", "wifi: " + isWifiConnected);
//        Log.d("IntentReceiver", "mobile: " + isMobileConnected);
//        Log.d("IntentReceiver", "sound: " + soundProfile);
        Log.d(TAG, intent.getAction());
        PhoneState.update(context);
        PhoneState.logIntent(intent);

	}

}
