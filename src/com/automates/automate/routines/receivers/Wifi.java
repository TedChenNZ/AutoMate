package com.automates.automate.routines.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

// Based on http://stackoverflow.com/questions/7329682/unable-to-listen-to-android-wi-fi-managers-state
public class Wifi extends BroadcastReceiver {
    private final String TAG = "WifiReceiver";

	@Override
    public void onReceive(Context context, Intent intent) {
        int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
        String msg = null;
        switch (state) {
        case WifiManager.WIFI_STATE_DISABLED:
            msg = "it is disabled";
            break;
        case WifiManager.WIFI_STATE_ENABLED:
            msg = "it is enabled";
            break;
        case WifiManager.WIFI_STATE_DISABLING:
            msg = "it is switching off";
            break;
        case WifiManager.WIFI_STATE_ENABLING:
            msg = "wifi is getting enabled";
            break;
        default:
            msg = "not working properly";
            break;
        }
        if (msg != null) {
            Log.d("************%%%%%%%%wifi state ", "WIFI" + msg);
            Toast.makeText(context, "Wifi state is " + msg, Toast.LENGTH_LONG).show();
        }
    }
}
