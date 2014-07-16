package com.automates.automate.routines;

public class Routine {

    private int id;
    private String name;
    private String event;
    private String eventCategory;
    private long time;
    private int day;
    private String location;
    private String wifi;
    private String mData;
    private int statusCode;
    
    
    public Routine() {
	// TODO Auto-generated constructor stub
    }
    
    public Routine(int id, String name, String event, String eventCategory, long time,
	    int day, String location, String wifi, String mData, int statusCode) {
	this.id = id;
	this.name = name;
	this.event = event;
	this.eventCategory = eventCategory;
	this.time = time;
	this.day = day;
	this.location = location;
	this.wifi = wifi;
	this.mData = mData;
	this.statusCode = statusCode;
    }

    @Override
    public String toString() {
	return "Routine [id=" + id + ", name=" + name + ", event=" + event
		+ ", eventCategory=" + eventCategory + ", time=" + time
		+ ", day=" + day + ", location=" + location + ", wifi=" + wifi
		+ ", mData=" + mData + ", statusCode=" + statusCode + "]";
    }


    public void activate() {
	// TODO Auto-generated method stub
	//ask user if location/wifi/data are conditions
	//alarm manager to do something for time based.
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
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


    public long getTime() {
        return time;
    }


    public void setTime(long actualTime) {
        this.time = actualTime;
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
