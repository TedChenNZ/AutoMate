package com.automates.automate.routines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.automates.automate.PhoneState;
import com.automates.automate.R;
import com.automates.automate.RoutineActivity;
import com.automates.automate.locations.UserLocation;

public class Routine {
	public static final String TIME = "Time";
	public static final String DAY = "Day";
	public static final String LOCATION = "Location";
	public static final String WIFI = "Wifi";
	public static final String MDATA = "Mobile Data";
	public static final String RINGER = "Ringer";
			
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



    public void activate() {
	// TODO Auto-generated method stub
	//ask user if location/wifi/data are conditions NOTIFICATION
	//alarm manager to do something for time based.
	RoutineActivity r = new RoutineActivity();
//	r.notification("Pattern recognised", "AutoMate has recognised a pattern - tap on this notification to configure the routine!", this.id);

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
    	//Routine.TIME, Routine.DAY, Routine.LOCATION, Routine.WIFI, Routine.MDATA
    	if (this.getHour() != -1 && this.getMinute() != -1) {
    		triggerList.add(Routine.TIME + ": " + this.getHour() + ":" + this.getMinute());
    	}
    	if (this.getDay() != null && !this.getDay().equals("")) {
    		// TODO: FIX TIS FOR MULTIPLE DAYS
    		triggerList.add(Routine.DAY + ": " + intToDay(Integer.parseInt(getDay())));
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
            
    		triggerList.add(Routine.LOCATION + ": " + s);
    	}
    	if (this.getWifi() != null && !this.getWifi().equals("")) {
    		triggerList.add(Routine.WIFI + ": " + this.getWifi());
    	}
    	if (this.getmData() != null && !this.getmData().equals("")) {
    		triggerList.add(Routine.MDATA + ": " + this.getmData());
    	}
    	
    	return triggerList;
    }

    public static int dayToInt(String day) {
        Map<String,Integer> mp = new HashMap<String,Integer>();
        
        mp.put("Sunday",0);
        mp.put("Monday",1);
        mp.put("Tuesday",2);
        mp.put("Wednesday",3);
        mp.put("Thrusday",4);
        mp.put("Friday",5);
        mp.put("Saturday",6);
        
        return mp.get(day).intValue();
   }
    
   public static String intToDay(int day) {
       Map<String,Integer> mp = new HashMap<String,Integer>();
       String d = "";
       mp.put("Sunday",0);
       mp.put("Monday",1);
       mp.put("Tuesday",2);
       mp.put("Wednesday",3);
       mp.put("Thrusday",4);
       mp.put("Friday",5);
       mp.put("Saturday",6);
       
       for (Entry<String, Integer> entry : mp.entrySet()) {
           if (entry.getValue().equals(day)) {
               d = entry.getKey();
           }
       }
       return d;
   }
   
   public static int ringerToInt(String ringer) {
	   
       Map<String,Integer> mp = new HashMap<String,Integer>();
       
       mp.put("Silent and No Vibrate",0);
       mp.put("Silent and Vibrate",1);
       mp.put("Normal and No Vibrate",2);
       mp.put("Normal and Vibrate",3);
       
       return mp.get(ringer).intValue();
   }
   
   public static String intToRinger(String ringer) {
	   String r = "";
	   
       Map<String,Integer> mp = new HashMap<String,Integer>();
       
       mp.put("Silent and No Vibrate",0);
       mp.put("Silent and Vibrate",1);
       mp.put("Normal and No Vibrate",2);
       mp.put("Normal and Vibrate",3);
       
       for (Entry<String, Integer> entry : mp.entrySet()) {
           if (entry.getValue().equals(ringer)) {
               r = entry.getKey();
           }
       }
       return r;
   }
   

}
