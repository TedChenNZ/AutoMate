package com.automates.automate.routines;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.automates.automate.PhoneState;

public class RoutineListener extends Service implements PropertyChangeListener{

	public RoutineListener(){
	    PhoneState.getInstance().addChangeListener(this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
	    // TODO Auto-generated method stub
	    
	}

	@Override
	public IBinder onBind(Intent arg0) {
	    // TODO Auto-generated method stub
	    return null;
	}

    }

