package com.automates.automate.routines;

public class Routine {

    private int id;
    private String name;
    private String event;
    private String eventCategory;
    private int hour;
    private int minute;
    private String day;
    private String location;
    private String wifi;
    private String mData;
    private int statusCode;
    
    
    public Routine() {
	// TODO Auto-generated constructor stub
    }
    
    public Routine(int id, String name, String event, String eventCategory, int hour, int minute,
	    String day, String location, String wifi, String mData, int statusCode) {
	this.id = id;
	this.name = name;
	this.event = event;
	this.eventCategory = eventCategory;
	this.hour = hour;
	this.minute = minute;
	this.day = day;
	this.location = location;
	this.wifi = wifi;
	this.mData = mData;
	this.statusCode = statusCode;
    }




    @Override
	public String toString() {
		return "Routine [id=" + id + ", name=" + name + ", event=" + event
				+ ", eventCategory=" + eventCategory + ", hour=" + hour
				+ ", minute=" + minute + ", day=" + day + ", location="
				+ location + ", wifi=" + wifi + ", mData=" + mData
				+ ", statusCode=" + statusCode + "]";
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public void activate() {
	// TODO Auto-generated method stub
	//ask user if location/wifi/data are conditions NOTIFICATION
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




    public String getDay() {
        return day;
    }


    public void setDay(String day) {
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
