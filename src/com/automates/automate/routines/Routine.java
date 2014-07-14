package com.automates.automate.routines;

public class Routine {

    private int id;
    private String event;
    private String eventCategory;
    private int time;
    private int day;
    private String location;
    private String wifi;
    private String mData;
    private int statusCode;
    
    
    public Routine(int id, String event, String eventCategory, int time,
	    int day, String location, String wifi, String mData, int statusCode) {
	this.id = id;
	this.event = event;
	this.eventCategory = eventCategory;
	this.time = time;
	this.day = day;
	this.location = location;
	this.wifi = wifi;
	this.mData = mData;
	this.statusCode = statusCode;
    }


    public Routine() {
	// TODO Auto-generated constructor stub
    }


    public int getId() {
        return id;
    }


    @Override
    public String toString() {
	return "Routine [id=" + id + ", event=" + event + ", eventCategory="
		+ eventCategory + ", time=" + time + ", day=" + day
		+ ", location=" + location + ", wifi=" + wifi + ", mData="
		+ mData + ", statusCode=" + statusCode + "]";
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getEvent() {
        return event;
    }


    public void setEvent(String event) {
        this.event = event;
    }


    public String getEventCategory() {
        return eventCategory;
    }


    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }


    public int getTime() {
        return time;
    }


    public void setTime(int time) {
        this.time = time;
    }


    public int getDay() {
        return day;
    }


    public void setDay(int day) {
        this.day = day;
    }


    public String getLocation() {
        return location;
    }


    public void setLocation(String location) {
        this.location = location;
    }


    public String getWifi() {
        return wifi;
    }


    public void setWifi(String wifi) {
        this.wifi = wifi;
    }


    public String getmData() {
        return mData;
    }


    public void setmData(String mData) {
        this.mData = mData;
    }


    public int getStatusCode() {
        return statusCode;
    }


    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    
    
    
    
}
