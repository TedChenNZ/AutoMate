package com.automates.automate.routines;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.Time;
import android.util.Log;

import com.automates.automate.PhoneState;
import com.automates.automate.pattern.StatusCode;
import com.automates.automate.routines.settings.SoundProfiles;
import com.automates.automate.routines.settings.Wifi;

public class RoutineApplier extends Service implements PropertyChangeListener{

    List<Routine> routines;
    private final static String TAG = "RoutineDB";

    public RoutineApplier(){
	PhoneState.getInstance().addChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
	checkRoutines();
    }

    public void checkRoutines(){
	routines = PhoneState.getRoutineDb().getAllRoutines();

	for(Routine r : routines){
	    if(checkPhoneConditions(r)){
		apply(r);
	    }
	}

    }

    private void apply(Routine r) {
	// TODO Auto-generated method stub
	Log.d(TAG, "Checking applications");
	if(r.getEventCategory().equals("Wifi")){
	    if(r.getEvent().equals("false")){
		Wifi.setWifiEnabled(this, false);
	    }
	    else{
		Wifi.setWifiEnabled(this, true);
	    }
	}
	else if(r.getEventCategory().equals("Ringer")){
	    SoundProfiles.setSoundProfile(this, Integer.parseInt(r.getEvent()));
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

	if (!r.getDay().equals(StatusCode.EMPTY) && r.getDay().indexOf(time.weekDay) == -1){conditions = false;};
	if (r.getHour() != (StatusCode.DECLINED) && r.getHour() != time.hour){conditions = false;};
	if (r.getMinute() != (StatusCode.DECLINED) && r.getMinute() != time.minute){conditions = false;};
	if (!r.getLocation().equals(StatusCode.EMPTY) && !r.getLocation().equals(PhoneState.getSetLocation())){conditions = false;};
	if (!r.getmData().equals(StatusCode.EMPTY) && !r.getmData().equals(Boolean.toString(PhoneState.isDataEnabled()))){conditions = false;};
	if (!r.getWifi().equals(StatusCode.EMPTY) && !r.getWifi().equals(PhoneState.getWifiBSSID())){conditions = false;};


	return conditions;
    }

}

