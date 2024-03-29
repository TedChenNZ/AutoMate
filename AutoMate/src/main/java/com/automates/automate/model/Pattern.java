package com.automates.automate.model;

import com.automates.automate.service.pattern.NotificationActivity;
import com.automates.automate.service.pattern.StatusCode;
import com.automates.automate.service.pattern.WeightManager;
import com.automates.automate.service.model.RoutineService;

import java.util.Calendar;

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


    //Transforms any empty strings to nulls
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


    //Creates a routine to be added into the database
    public void addToRoutineDB() {
	    Routine r = new Routine();
	    r.setName("Routine " + this.id);
	    r.setEvent(this.event);
	    r.setEventCategory(this.eventCategory);
	    Calendar c = Calendar.getInstance();
	    c.setTimeInMillis(this.actualTime);
	    r.setHour(c.get(Calendar.HOUR_OF_DAY));
	    r.setMinute(round((c.get(Calendar.MINUTE)), 5));


        r.setDay(Integer.toString(1234560)); //everyday
//	    r.setDay("" + this.day);
	    r.setLocation(this.location);
//	    r.setmData(this.mData);
//	    r.setWifi(this.wifi);
	    r.setStatusCode(StatusCode.IN_DEV);
	    Routine r2 = RoutineService.getInstance().addRoutine(r);
	    activate(r2);
    }
    
    private int round(double i, int v) {
    	return (int) (Math.round(i/v)*v);
    	
    }

    //Called when pattern is recognised.
    public void activate(Routine r) {
	// TODO Auto-generated method stub
	//ask user if location/wifi/data are conditions NOTIFICATION
	//alarm manager to do something for time based.
//    	this.setStatusCode(StatusCode.AWAITING_APPROVAL);
	NotificationActivity nM = new NotificationActivity();
	nM.notification("Routine Recognised", "AutoMate has recognised a routine - tap on this notification to configure the routine!", r.getId(), this.getId());

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
    }

    //Used to check if pattern can be recognised.
    public boolean checkThresholdAndStatusCode(){
	if (this.weight >= WeightManager.suggestedWeight && this.statusCode == StatusCode.IN_DEV){
	    return true;
	}
	return false;
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