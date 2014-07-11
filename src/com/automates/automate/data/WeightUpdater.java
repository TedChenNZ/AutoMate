package com.automates.automate.data;

import android.util.Log;

import com.automates.automate.PhoneState;

public class WeightUpdater implements WeightManager {

    private Pattern p;
    private long actualTime;
    
    private Pattern oldP;
    private double oldWeight;
    private int id;
    
    private long timeDiff; 
    private int weeksPast, daysPast;
    
    private final long weekInMS = 604800000;
    private final long dayInMS = 86400000;
    
    
    public WeightUpdater(Pattern p, int id){
	this.p = p;
	this.id = id;
	actualTime = p.getActualTime();
	oldPset(id);
    }

    private void oldPset(int id){
	oldP = PhoneState.getDb().getPattern(id);
	oldWeight = oldP.getWeight();
	timeDiff = actualTime - oldP.getActualTime();
    }
    
    public Pattern updatePattern() {
	double newWeight;
	
	weeksPast = Math.round(timeDiff/weekInMS); 
	daysPast = Math.round(timeDiff/dayInMS);
	
	if(oldP.getDay() == p.getDay()){
//	    newWeight = oldWeight + WeightManager.initialWeight/weeksPast;
	    newWeight = oldWeight + 500;  
	}
	else{
	    newWeight = oldWeight + WeightManager.initialWeight/daysPast;
	}
	
	p.setWeight(newWeight);
	p.setId(id);
	Log.d("PatternController", "" + newWeight);
	
	return p;
    }
}
