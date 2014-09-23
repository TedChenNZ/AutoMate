package com.automates.automate.service.pattern;

import android.text.format.Time;

import com.automates.automate.service.PhoneService;
import com.automates.automate.model.Pattern;
import com.automates.automate.service.model.PatternService;
import com.automates.automate.service.settings.Settings;

import java.util.Date;
import java.util.List;

public class PatternGenerator {
	private Pattern p = new Pattern();
    private long time = 0;


	public PatternGenerator(String actionCategory, String action) {
		p.setEvent(action);
		p.setEventCategory(actionCategory);
        this.time = System.currentTimeMillis();
		generatePattern();
	}

	public PatternGenerator(Pattern p) {
		this.p = p;
        time = p.getActualTime();
        timeSet();
	}

	/* (non-Javadoc)
	 * @see com.automates.automate.data.PatternControl#generatePattern()
	 */

	public Pattern generatePattern(){

		p.setLocation(PhoneService.getInstance().getSetLocation());
		p.setWifi(PhoneService.getInstance().getWifiBSSID());
		p.setData(Boolean.toString(PhoneService.getInstance().isDataEnabled()));

		if (p.getEventCategory().equals(Settings.WIFI)) {
			p.setWifi("");
		}
		if (p.getEventCategory().equals(Settings.MDATA)) {
			p.setData("");
		}

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

	public Pattern getPattern(){
		return p;
	}

	private void timeSet(){
		Time t = new Time();
        t.set(time);
		p.setDay(t.weekDay);
		p.setActualTime(time);
		timeTransform();
	}

	@SuppressWarnings("deprecation")
	private void timeTransform(){
		Date d = new Date(time);
		d.setSeconds(0);
		d.setMinutes(0);
		d.setHours(0);
		//int day = d.getDay();

		long startOfDay = d.getTime();

		long diff = time - startOfDay;
		int intervals = (int) Math.ceil(diff/WeightManager.timeDivision);

		p.setTime(intervals);
	}

	/* (non-Javadoc)
	 * @see com.automates.automate.data.PatternControl#updateDatabase()
	 */

	public void updateDatabase() {
		int id = getInstanceFromDB();
		if(id != -1){
			
			Pattern oldP = PatternService.getInstance().getPattern(id);
			Pattern newP = new WeightUpdater(p, oldP).updatePattern();
//			Pattern newP = new WeightUpdater(p, id).updatePattern();
			boolean threshold = newP.checkThresholdAndStatusCode();
			if(threshold){
				newP.setStatusCode(StatusCode.AWAITING_APPROVAL);
				newP.addToRoutineDB();
			}
            PatternService.getInstance().updatePattern(newP);
//			Log.d(TAG, "time: " + newP.getActualTime());
//			Log.d(TAG, "updating pattern: " + newP.getWeight());
		}
		else{
//			Log.d(TAG, "adding pattern");
            PatternService.getInstance().addPattern(p);
		}
	}

	private int getInstanceFromDB(){
		int id = -1;
		List<Pattern> ps = PatternService.getInstance().getAllPatterns();
		for(Pattern pList : ps){
			if (pList.compare(p)){
				id = pList.getId();
			}
		}
//		Log.d(TAG, "id is: " + id);
		return id;
	}




}
