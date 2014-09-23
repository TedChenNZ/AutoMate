package com.automates.automate.service.settings;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class Wifi {
	
	public Wifi() {}
	
	public static void setWifiEnabled(Context context, boolean enabled) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE); 
		wifiManager.setWifiEnabled(enabled);
	}
	
	public static boolean getWifiEnabled(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE); 
		return wifiManager.isWifiEnabled();
	}
	
	public static String getWifiBSSID(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifiManager.getConnectionInfo();
		String BSSID = "false";
		if (info.getBSSID() != null) {
			BSSID = info.getBSSID();
		}
		
		return BSSID;
		
	}

}
