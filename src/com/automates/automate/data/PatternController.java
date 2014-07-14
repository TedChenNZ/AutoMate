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
	p.setEvent(action);
	p.setEventCategory(actionCategory);
	generatePattern();
    }

    public Pattern generatePattern(){

	p.setLocation(PhoneState.getSetLocation());
	p.setWifi(PhoneState.getWifiBSSID());
	p.setData(Boolean.toString(PhoneState.isDataEnabled()));

	timeSet();
	//TODO weight and status code checking
	p.setWeekWeight(WeightManager.initialZeroWeight);
	p.setWeight(WeightManager.initialWeight);
	p.setStatusCode(StatusCode.IN_DEV);
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
	String timeT = "" + PhoneState.getTime();
	timeTransform();
	

    }
    
    @SuppressWarnings("deprecation")
    private void timeTransform(){
	long current = PhoneState.getTime();
	Date d = new Date(PhoneState.getTime());
	d.setSeconds(0);
	d.setMinutes(0);
	d.setHours(0);
	int day = d.getDay();
	
	long startOfDay = d.getTime();
	
	long diff = current - startOfDay;
	int intervals = (int) Math.ceil(diff/timeDivision);
	String sRes = "" + day + intervals;
	
	int result = Integer.parseInt(sRes);
	
	p.setTime(result);
    }

    public void updateDatabase() {
	Log.d(TAG, p.toString());
	int id = getInstanceFromDB();
	if(id != -1){
	    Pattern newP = new WeightUpdater(p, id).updatePattern();
	    PhoneState.getDb().updatePattern(newP);
	    Log.d(TAG, "updating pattern: " + newP.getWeight());
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
