package com.automates.automate.settings;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.media.AudioManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;

public class RingerProfiles {
    public static final int SILENT_NO_VIBRATE = 0;
    public static final int SILENT_VIBRATE = 1;
    public static final int NORMAL_NO_VIBRATE = 2;
    public static final int NORMAL_VIBRATE = 3;
    
    public static final Map<String,Integer> ringerMap;
    static {
    	ringerMap = new LinkedHashMap<String,Integer>();
    	ringerMap.put("Silent and No Vibrate",SILENT_NO_VIBRATE);
    	ringerMap.put("Silent and Vibrate",SILENT_VIBRATE);
    	ringerMap.put("Normal and No Vibrate",NORMAL_NO_VIBRATE);
    	ringerMap.put("Normal and Vibrate",NORMAL_VIBRATE);
    }
    
    public RingerProfiles() {}
    
	public static void setSoundProfile(Context context, int soundProfile) {
        final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        switch (soundProfile) {
            case SILENT_NO_VIBRATE:
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                break;
            case SILENT_VIBRATE:
                audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                break;
            case NORMAL_NO_VIBRATE:
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//              audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);
//                audioManager.setVibrateSetting(1, 0);
//                audioManager.setVibrateSetting(0, 0);
                Settings.System.putInt(context.getContentResolver(), "vibrate_when_ringing", 0);
                

                
                break;	
            case NORMAL_VIBRATE:
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//              audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_ON);
//              audioManager.setVibrateSetting(1, 1);
//              audioManager.setVibrateSetting(0, 1);
                
                Settings.System.putInt(context.getContentResolver(), "vibrate_when_ringing", 1);
                break;
        }
        
    }
    
    
    public static int getMode(Context context) {
        final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int ringer = audioManager.getRingerMode();
        int vibrate = 0;
        int result = 0;
        try {
			vibrate = Settings.System.getInt(context.getContentResolver(), "vibrate_when_ringing");
		} catch (SettingNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        switch (ringer) {
        	case AudioManager.RINGER_MODE_SILENT: result = SILENT_NO_VIBRATE; break;
        	case AudioManager.RINGER_MODE_VIBRATE: result = SILENT_VIBRATE; break;
        	case AudioManager.RINGER_MODE_NORMAL:
        		if (vibrate == 0)
        			result = NORMAL_NO_VIBRATE;
        		else 
        			result = NORMAL_VIBRATE;
        		break;
        }
        
        return result;
    }
    
    public static int ringerToInt(String ringer) {
        return ringerMap.get(ringer).intValue();
    }
    
    public static String intToRinger(String ringer) {
 	   String r = "";
       for (Entry<String, Integer> entry : ringerMap.entrySet()) {
           if (entry.getValue().equals(ringer)) {
               r = entry.getKey();
           }
       }
       return r;
    }
    
}
