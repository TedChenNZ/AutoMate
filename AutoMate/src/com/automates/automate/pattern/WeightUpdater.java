package com.automates.automate.pattern;

import com.automates.automate.pattern.WeightManager;

public class WeightUpdater implements WeightManager {

	private Pattern p;
	private long actualTime;

	private Pattern oldP;
	private double oldWeight;
	private double oldWeekWeight;
	private int id;

	private long timeDiff; 

	private final double weekInMS = 604800000;
	private final double dayInMS = 86400000;

	public WeightUpdater(Pattern p, Pattern oldP){
		this.p = p;
		this.oldP = oldP;
		this.id = oldP.getId();
		actualTime = p.getActualTime();
		oldWeight = oldP.getWeight();
		oldWeekWeight = oldP.getWeekWeight();
		timeDiff = actualTime - oldP.getActualTime();
		this.p.setStatusCode(oldP.getStatusCode());
	}

	public Pattern updatePattern() {
		double newWeight, newWeekWeight;
		int weeksPast, daysPast;

		weeksPast = (int) Math.round(timeDiff/weekInMS); 
		daysPast = (int) Math.round(timeDiff/dayInMS);


		if(oldP.getDay() == p.getDay()){

			if(weeksPast != 0){
				newWeight = oldWeight + WeightManager.initialWeight/weeksPast;
				newWeekWeight = oldWeekWeight + WeightManager.initialWeight/weeksPast;
				//		Log.d("PatternController", "It's been more than a week - update/increase!");
			}
			else{
				//		Log.d("PatternController", "Same day - so same weight!");
				newWeight = oldWeight;
				newWeekWeight = oldWeekWeight;

			}

			p.setWeekWeight(newWeekWeight - oldWeekWeight);
			//	    Log.d("PatternController", "New week weight is: " + newWeekWeight);
		}
		else{
			if(daysPast != 0){
				newWeight = oldWeight + WeightManager.initialWeight/daysPast;
				timeSet();
			}
			else{
				//		Log.d("PatternController", "Should never hit this");
				newWeight = oldWeight;
			}
		}

		p.setId(id);
		p.setWeight(newWeight);
		//	Log.d("PatternController", "New weight is: " + newWeight);

		return p;
	}

	private void timeSet(){
//		Time time = new Time();
//		time.setToNow();
//		p.setDay(time.weekDay);
//		p.setActualTime(PhoneState.getTime());
		
	}
}
