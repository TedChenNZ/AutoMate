package com.automates.automate.data;

import android.util.Log;

import com.automates.automate.PhoneState;

public class WeightUpdater implements WeightManager {

    private Pattern p;
    private long actualTime;

    private Pattern oldP;
    private double oldWeight;
    private double oldWeekWeight;
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
	oldWeekWeight = oldP.getWeekWeight();
	timeDiff = actualTime - oldP.getActualTime();
    }

    public Pattern updatePattern() {
	double newWeight, newWeekWeight;

	weeksPast = Math.round(timeDiff/weekInMS); 
	daysPast = Math.round(timeDiff/dayInMS);


	if(oldP.getDay() == p.getDay()){

	    if(weeksPast != 0){
		newWeight = oldWeight + WeightManager.initialWeight/weeksPast;
		newWeekWeight = oldWeekWeight + WeightManager.initialWeight/weeksPast;
	    }
	    else{
		newWeight = oldWeight;
		newWeekWeight = oldWeekWeight;
	    }

	    p.setWeekWeight(newWeekWeight - oldWeekWeight);
	    Log.d("PatternController", "New week weight is: " + newWeekWeight);
	}
	else{
	    if(daysPast != 0){
		newWeight = oldWeight + WeightManager.initialWeight/daysPast;
	    }
	    else{
		newWeight = oldWeight;
	    }
	}

	p.setWeight(newWeight);
	p.setId(id);
	Log.d("PatternController", "New weight is: " + newWeight);

	return p;
    }
}
