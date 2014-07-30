package com.automates.automate.pattern;

import java.util.Date;
import java.util.List;

import android.text.format.Time;
import android.util.Log;

import com.automates.automate.PhoneState;

public class PatternController implements PatternControl {
	private Pattern p = new Pattern();

	private final String TAG = "PatternController";

	public PatternController(String actionCategory, String action) {
		p.setEvent(action);
		p.setEventCategory(actionCategory);
		generatePattern();
	}

	/* (non-Javadoc)
	 * @see com.automates.automate.data.PatternControl#generatePattern()
	 */
	@Override
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

	/* (non-Javadoc)
	 * @see com.automates.automate.data.PatternControl#getPattern()
	 */
	@Override
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
		long current = PhoneState.getTime();
		Date d = new Date(PhoneState.getTime());
		d.setSeconds(0);
		d.setMinutes(0);
		d.setHours(0);
		int day = d.getDay();

		long startOfDay = d.getTime();

		long diff = current - startOfDay;
		int intervals = (int) Math.ceil(diff/WeightManager.timeDivision);
		String sRes = "" + intervals;

		int result = Integer.parseInt(sRes);

		p.setTime(result);
	}

	/* (non-Javadoc)
	 * @see com.automates.automate.data.PatternControl#updateDatabase()
	 */
	@Override
	public void updateDatabase() {
		Log.d(TAG, p.toString());
		int id = getInstanceFromDB();
		if(id != -1){
			Pattern newP = new WeightUpdater(p, id).updatePattern();
			PhoneState.getPatternDb().updatePattern(newP);
			Log.d(TAG, "updating pattern: " + newP.getWeight());
		}
		else{
			Log.d(TAG, "adding pattern");
			PhoneState.getPatternDb().addPattern(p);
		}
	}

	private int getInstanceFromDB(){
		int id = -1;
		List<Pattern> ps = PhoneState.getPatternDb().getAllPatterns();
		for(Pattern pList : ps){
			if (pList.compare(p)){
				id = pList.getId();
			}
		}
		Log.d(TAG, "id is: " + id);
		return id;
	}




}
