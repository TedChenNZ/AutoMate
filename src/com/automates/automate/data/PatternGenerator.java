package com.automates.automate.data;

import java.util.Date;

import android.text.format.Time;

import com.automates.automate.PhoneState;

public class PatternGenerator {
    private Pattern p = null;
    private final int timeDivision = 900000; //15 minutes

    public PatternGenerator(String actionCategory, String action) {
	// TODO Auto-generated constructor stub
	p.setAction(action);
	p.setActionCategory(actionCategory);
	generatePattern();
    }

    public Pattern generatePattern(){

	p.setLocation(PhoneState.getLocation().toString());
	p.setWifi(Boolean.toString(PhoneState.isWifiEnabled()));
	p.setData(Boolean.toString(PhoneState.isDataEnabled()));

	timeSet();
	//TODO weight and statuscode checking
	p.setWeight(1);
	p.setStatusCode(0);
	return p;

    }
    
    private void timeSet(){
	Time time = new Time();
	time.setToNow();
	p.setDay(time.weekDay);
	p.setActualTime(PhoneState.getTime());
	
	timeTransform();
	

    }
    
    @SuppressWarnings("deprecation")
    private void timeTransform(){
	int current = PhoneState.getTime();
	Date d = new Date(PhoneState.getTime());
	d.setSeconds(0);
	d.setMinutes(0);
	d.setHours(0);
	int day = d.getDay();
	
	int startOfDay = (int) d.getTime();
	
	int diff = current - startOfDay;
	int intervals = (int) Math.ceil(diff/timeDivision);
	String sRes = "" + day + intervals;
	
	int result = Integer.parseInt(sRes);
	
	p.setTime(result);
    }




}
