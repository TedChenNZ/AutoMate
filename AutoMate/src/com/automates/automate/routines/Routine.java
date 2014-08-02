package com.automates.automate.routines;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.automates.automate.PhoneState;
import com.automates.automate.locations.UserLocation;
import com.automates.automate.settings.Settings;

public class Routine {

			
    private String day;
    private String event;
    private String eventCategory;
    private int hour;
    private int id;
    private String location;
    private String mData;
    private int minute;
    private String name;
    private int statusCode;
    private String wifi;
    
    public static final Map<String,Integer> daysMap;
    static {
    	daysMap = new LinkedHashMap<String,Integer>();
        daysMap.put("Sunday",0);
        daysMap.put("Monday",1);
        daysMap.put("Tuesday",2);
        daysMap.put("Wednesday",3);
        daysMap.put("Thursday",4);
        daysMap.put("Friday",5);
        daysMap.put("Saturday",6);
    }

    public Routine() {
    	this.name = "";
    	this.event = "";
    	this.eventCategory = "";
    	this.day = "";
    	this.location = "";
    	this.wifi = "";
    	this.mData = "";
    	this.minute = -1;
    	this.hour = 01;
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




    public String getDay() {
	return day;
    }

    public String getEvent() {
	return event;
    }

    public String getEventCategory() {
	return eventCategory;
    }

    public int getHour() {
	return hour;
    }

    public int getId() {
	return id;
    }

    public String getLocation() {
	return location;
    }

    public String getmData() {
	return mData;
    }


    public int getMinute() {
	return minute;
    }


    public String getName() {
	return name;
    }


    public int getStatusCode() {
	return statusCode;
    }


    public String getWifi() {
	return wifi;
    }


    public void setDay(String day) {
	this.day = day;
    }


    public void setEvent(String event) {
	this.event = event;
    }




    public void setEventCategory(String eventCategory) {
	this.eventCategory = eventCategory;
    }


    public void setHour(int hour) {
	this.hour = hour;
    }


    public void setId(int id) {
	this.id = id;
    }


    public void setLocation(String location) {
	this.location = location;
    }


    public void setmData(String mData) {
	this.mData = mData;
    }


    public void setMinute(int minute) {
	this.minute = minute;
    }


    public void setName(String name) {
	this.name = name;
    }


    public void setStatusCode(int statusCode) {
	this.statusCode = statusCode;
    }


    public void setWifi(String wifi) {
	this.wifi = wifi;
    }


    @Override
    public String toString() {
	return "Routine [id=" + id + ", name=" + name + ", event=" + event
		+ ", eventCategory=" + eventCategory + ", hour=" + hour
		+ ", minute=" + minute + ", day=" + day + ", location="
		+ location + ", wifi=" + wifi + ", mData=" + mData
		+ ", statusCode=" + statusCode + "]";
    }

    public String readableForm() {
	return "Set " + eventCategory + " to " + "event" + " at " + hour + ":" + "minute" + " depending on conditions."; 
    }
    
    public List<String> activeTriggerList() {
    	List<String> triggerList = new ArrayList<String>();
    	//Settings.TIME, Settings.DAY, Settings.LOCATION, Settings.WIFI, Settings.MDATA
    	if (this.getHour() != -1 && this.getMinute() != -1) {
    		triggerList.add(Settings.TIME + ": " + this.getHour() + ":" + this.getMinute());
    	}
    	if (this.getDay() != null && !this.getDay().equals("")) {
    		// TODO: FIX TIS FOR MULTIPLE DAYS
    		triggerList.add(Settings.DAY + ": " + intToDay(Integer.parseInt(getDay())));
    	}
    	
    	if (this.getLocation() != null && !this.getLocation().equals("")) {
    		String s = this.getLocation();
    		try {
    			int loc = Integer.parseInt(this.getLocation());
    			for (UserLocation ul: PhoneState.getLocationsList()) {
                    if (ul.getId() == (loc)) {
                        s = ul.getName();
                        break;
                    }
                }
    		} catch (NumberFormatException e) {
    			
    		}
            
    		triggerList.add(Settings.LOCATION + ": " + s);
    	}
    	if (this.getWifi() != null && !this.getWifi().equals("")) {
    		triggerList.add(Settings.WIFI + ": " + this.getWifi());
    	}
    	if (this.getmData() != null && !this.getmData().equals("")) {
    		triggerList.add(Settings.MDATA + ": " + this.getmData());
    	}
    	
    	return triggerList;
    }

    public static int dayToInt(String day) {

        
        return daysMap.get(day).intValue();
   }
    
   public static String intToDay(int day) {
       String d = "";

       for (Entry<String, Integer> entry : daysMap.entrySet()) {
           if (entry.getValue().equals(day)) {
               d = entry.getKey();
           }
       }
       return d;
   }
   

   

}
