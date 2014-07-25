package com.automates.automate.routines.settings;

import android.content.Context;
import android.media.AudioManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;

public class SoundProfiles {
    public static final int SILENT_NO_VIBRATE = 0;
    public static final int SILENT_VIBRATE = 1;
    public static final int NORMAL_NO_VIBRATE = 2;
    public static final int NORMAL_VIBRATE = 3;
    
    public SoundProfiles() {}
    
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
    
}
