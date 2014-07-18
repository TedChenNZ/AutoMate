package com.automates.automate.routines;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.Time;

import com.automates.automate.PhoneState;

public class RoutineListener extends Service implements PropertyChangeListener{

    List<Routine> routines;

    public RoutineListener(){
	PhoneState.getInstance().addChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
	routines = PhoneState.getRoutineDb().getAllRoutines();

	for(Routine r : routines){
	    if(checkPhoneConditions(r)){apply(r);}
	}
	
    }

    private void apply(Routine r) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public IBinder onBind(Intent arg0) {
	// TODO Auto-generated method stub
	return null;
    }

    private boolean checkPhoneConditions(Routine r){
	boolean conditions = false;

	Time time = new Time();
	time.setToNow();

	if (r.getDay().indexOf(time.weekDay) == -1){conditions = false;};
	if (r.getHour() != time.hour){conditions = false;};
	if (r.getMinute() != time.minute){conditions = false;};
	if (!r.getLocation().equals(PhoneState.getSetLocation())){conditions = false;};
	if (!r.getmData().equals(Boolean.toString(PhoneState.isDataEnabled()))){conditions = false;};
	if (!r.getWifi().equals(PhoneState.getWifiBSSID())){conditions = false;};


	return conditions;
    }

}

