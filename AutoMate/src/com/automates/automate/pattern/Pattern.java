package com.automates.automate.pattern;

import java.util.Calendar;

import com.automates.automate.PhoneState;
import com.automates.automate.RoutineActivity;
import com.automates.automate.routines.Routine;
import com.automates.automate.settings.NotifyManager;

/**
 * This class defines the structure for a pattern to be entered into the database. Also contains methods to compare for unique patterns.
 * @author Dhanish
 *
 */

public class Pattern {

	private int id;
	private String event;
	private String eventCategory;
	private int time;
	private long actualTime;
	private int day;
	private String location;
	private String wifi;
	private String mData;
	private double weekWeight;
	private double weight;
	private int statusCode;

	public Pattern(){}

	public Pattern(String eventCategory, String event, int time, long actualTime, int day, String location, String wifi, String mData, double weekWeight, double weight, int statusCode) {
		this.eventCategory = eventCategory;
		this.event = event;
		this.time = time;
		this.actualTime = actualTime;
		this.day = day;
		this.location = location;
		this.wifi = wifi;
		this.mData = mData;
		this.weekWeight = weekWeight;
		this.weight = weight;
		this.statusCode = statusCode;
		nullTransform();
	}

	private void nullTransform() {
		this.event = stringToNull(this.event);
		this.eventCategory = stringToNull(this.eventCategory);
		this.event = stringToNull(this.event);
		this.location = stringToNull(this.location);
		this.wifi = stringToNull(this.wifi);
		this.mData = stringToNull(this.mData);
	}

	private String stringToNull(String str){
		if(str == null){
			return "";
		}
		else{
			return str;
		}
	}

	/**
	 * Used to check if the unique values of a pattern match with another 
	 * @param p The pattern to be compared against.
	 * @return a boolean indicating the match.
	 */
	public boolean compare(Pattern p){
		if(p.getEventCategory().equals(this.getEventCategory()) &&
				p.getEvent().equals(this.getEvent()) &&
				p.getTime() == this.getTime()){
			return true;
		}
		return false;
	}


	private void addToRoutineDB() {
		if(this.statusCode != StatusCode.IMPLEMENTED){
			this.statusCode = StatusCode.IMPLEMENTED;
			Routine r = new Routine();
			r.setName("Routine " + this.id);
			r.setEvent(this.event);
			r.setEventCategory(this.eventCategory);

			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(this.actualTime);
			r.setHour(c.get(Calendar.HOUR_OF_DAY));
			r.setMinute(c.get(Calendar.MINUTE));

			r.setDay("" + this.day);
			r.setLocation(this.location);
			r.setmData(this.mData);
			r.setWifi(this.wifi);
			r.setStatusCode(StatusCode.IN_DEV);
			Routine r2 = PhoneState.getRoutineDb().addRoutine(r);
			activate(r2);
		}
	}

	public void activate(Routine r) {
		// TODO Auto-generated method stub
		//ask user if location/wifi/data are conditions NOTIFICATION
		//alarm manager to do something for time based.
		NotifyManager nM = new NotifyManager();
		nM.notification("Pattern recognised", "AutoMate has recognised a pattern - tap on this notification to configure the routine!", r.getId());

	}


	public String getEvent() {
		return this.event;
	}

	public String getEventCategory() {
		return this.eventCategory;
	}

	public long getActualTime() {
		return this.actualTime;
	}

	public String getData() {
		return this.mData;
	}

	public int getDay() {
		return this.day;
	}

	public int getId() {
		return this.id;
	}

	public String getLocation() {
		return this.location;
	}

	public int getStatusCode() {
		return this.statusCode;
	}

	public int getTime() {
		return this.time;
	}

	public double getWeight() {
		return this.weight;
	}

	public String getWifi() {
		return this.wifi;
	}

	public void setEvent(String input){
		this.event = input;
		nullTransform();
	}

	public void setEventCategory(String input){
		this.eventCategory = input;
		nullTransform();
	}

	public void setActualTime(long input){
		this.actualTime = input;
	}

	public void setData(String input){
		this.mData = input;
		nullTransform();
	}

	public void setDay(int weekDay){
		this.day = weekDay;
		nullTransform();
	}

	public void setId(int input){
		this.id = input;
	}

	public void setLocation(String input){
		this.location = input;
		nullTransform();
	}

	public void setStatusCode(int input){
		this.statusCode = input;
	}

	public void setTime(int input){
		this.time = input;
	}

	public void setWeight(double input){
		this.weight = input;
		if (this.weight >= WeightManager.suggestedWeight){addToRoutineDB();}
	}

	public void setWifi(String input){
		this.wifi = input;
		nullTransform();
	}

	public double getWeekWeight() {
		return weekWeight;
	}

	public void setWeekWeight(double weekWeight) {
		this.weekWeight = weekWeight;
	}

	@Override
	public String toString() {
		return "Pattern [id=" + id + ", action=" + event + ", actionCategory="
				+ eventCategory + ", time=" + time + ", actualTime="
				+ actualTime + ", day=" + day + ", location=" + location
				+ ", wifi=" + wifi + ", mData=" + mData + ", weekWeight="
				+ weekWeight + ", weight=" + weight + ", statusCode="
				+ statusCode + "]";
	}


}