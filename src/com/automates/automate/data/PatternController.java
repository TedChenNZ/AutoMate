package com.automates.automate.data;

import java.util.Date;
import java.util.List;

import android.text.format.Time;
import android.util.Log;

import com.automates.automate.PhoneState;

public class PatternController {
    private Pattern p = new Pattern();
    private final int timeDivision = 900000; //15 minutes
    private final String TAG = "PatternController";

    public PatternController(String actionCategory, String action) {
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
    
    public Pattern getPattern(){
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

    public void updateDatabase() {
	
	int id = getInstanceFromDB();
	if(id != -1){
	    Log.d(TAG, "updating pattern");
	}
	else{
	    Log.d(TAG, "adding pattern");
	    PhoneState.getDb().addPattern(p);
	}
    }
    
    private int getInstanceFromDB(){
	int id = -1;
	List<Pattern> ps = PhoneState.getDb().getAllPatterns();
	for(Pattern pList : ps){
	    if (pList.compare(p)){
		id = pList.getId();
	    }
	}
	Log.d(TAG, "id is: " + id);
	return id;
    }




}
