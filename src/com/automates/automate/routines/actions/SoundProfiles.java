package com.automates.automate.routines.actions;

import android.content.Context;
import android.media.AudioManager;

public class SoundProfiles {
	
	public SoundProfiles() {}
	
	public static void setSilent(Context context) {
		final AudioManager mode = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		mode.setRingerMode(AudioManager.RINGER_MODE_SILENT);
	}
	
	public static void setNormal(Context context) {
		final AudioManager mode = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		mode.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
	}
	
	public static void setVibrate(Context context) {
		final AudioManager mode = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		mode.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
	}
	
}
