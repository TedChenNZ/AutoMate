package com.automates.automate.routines;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.Time;
import android.util.Log;

import com.automates.automate.PhoneState;
import com.automates.automate.pattern.StatusCode;
import com.automates.automate.settings.Data;
import com.automates.automate.settings.RingerProfiles;
import com.automates.automate.settings.Settings;
import com.automates.automate.settings.Wifi;

public class RoutineApplier extends Service implements PropertyChangeListener{

	List<Routine> routines;
	public Context context;
	private final static String TAG = "RoutineDB";

	public RoutineApplier(Context context){
		PhoneState.getInstance().addChangeListener(this);
		this.context = context;
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		checkRoutines();
	}

	public void checkRoutines(){
		routines = PhoneState.getRoutineDb().getAllRoutines();
		if (routines != null) {
			Log.d(TAG, "Checking applications");
			for(Routine r : routines){
				if(checkPhoneConditions(r)){
					apply(r);
				}
			}
		}

	}

	private void apply(Routine r) {
		// TODO Auto-generated method stub
		Log.d(TAG, "Actioning routine " + r.getName());
		if(r.getEventCategory().equalsIgnoreCase(Settings.WIFI)){
			if(r.getEvent().equalsIgnoreCase("false")){
				Wifi.setWifiEnabled(context, false);
				Log.d(TAG, "Wifi turned off");
			}
			else{
				Wifi.setWifiEnabled(context, true);
				Log.d(TAG, "Wifi turned on");
			}
		}
		else if(r.getEventCategory().equalsIgnoreCase(Settings.RINGER)){
			RingerProfiles.setSoundProfile(this, Integer.parseInt(r.getEvent()));
			Log.d(TAG, "Ringer changed to " + r.getEvent());
		}
		else if(r.getEventCategory().equalsIgnoreCase(Settings.MDATA)){
			if(r.getEvent().equalsIgnoreCase("false")){
				Data.setDataEnabled(context, false);
				Log.d(TAG, "Data turned off");
			}
			else{
				Data.setDataEnabled(context, true);
				Log.d(TAG, "Data turned on");
			}
		}

	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean checkPhoneConditions(Routine r){
		boolean conditions = true;

		Time time = new Time();
		time.setToNow();
		if (r.getEventCategory() == null || r.getEvent() == null || r.getDay() == null || r.getLocation() == null || r.getWifi() == null || r.getmData() == null) {
			return false;
		}

		if (!r.getDay().equals(StatusCode.EMPTY) && r.getDay().indexOf(time.weekDay) == -1){conditions = false;};
		if (r.getHour() != (StatusCode.DECLINED) && r.getHour() != time.hour){conditions = false;};
		if (r.getMinute() != (StatusCode.DECLINED) && r.getMinute() != time.minute){conditions = false;};
		if (!r.getLocation().equals(StatusCode.EMPTY) && !r.getLocation().equals(PhoneState.getSetLocation())){conditions = false;};
		if (!r.getmData().equals(StatusCode.EMPTY) && !r.getmData().equals(Boolean.toString(PhoneState.isDataEnabled()))){conditions = false;};
		if (!r.getWifi().equals(StatusCode.EMPTY) && !r.getWifi().equals(PhoneState.getWifiBSSID())){conditions = false;};

		if(r.getStatusCode() != StatusCode.IMPLEMENTED){
			conditions = false;
		}
		return conditions;
	}

}

