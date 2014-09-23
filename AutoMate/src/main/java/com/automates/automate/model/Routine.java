package com.automates.automate.model;

import com.automates.automate.service.model.UserLocationService;
import com.automates.automate.service.settings.RingerProfiles;
import com.automates.automate.service.settings.Settings;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
        daysMap.put("Weekdays", 12345);
        daysMap.put("Weekends", 60);
        daysMap.put("Every Day", 1234560);
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
    	this.hour = -1;
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

    public String eventString() {
    	String e = "";
    	if (eventCategory.equals(Settings.RINGER)) {
    		e = RingerProfiles.intToRinger(Integer.parseInt(event));
    	} else if (eventCategory.equals(Settings.WIFI) || eventCategory.equals(Settings.MDATA)) {
    		e = onOrOff(event);
    	}
    	return "Set " + eventCategory + " to " + e; 
    }
    
    public String triggerString() {
    	String s = "";
    	//int id, String name, String event, String eventCategory, int hour, int minute,
	    //String day, String location, String wifi, String mData, int statusCode
    	if (!day.equals("") && day != null) {
    			s = s + "on " + intToDay(Integer.parseInt(day)) + " ";
    	}
    	if ((hour != -1) && (minute != -1)) {
    		s = s + "at " + String.format("%02d", hour) + ":" + String.format("%02d", minute) + " ";
    	}
    	if (!location.equals("") && location != null) {
    		try {
                int i = Integer.parseInt(location);
                String loc = "";
                if (i == -1) {
                    loc = "Unknown Location";
                } else {
                    loc = UserLocationService.getInstance().getUserLocationFromID(i).getName();
                }
    			s = s + "at " + loc + " ";
    	
    		} catch (Exception e) {
    			s = s + "at UNDEFINED LOCATION " + location;
    		} 
    	}
    	
    	if (!wifi.equals("") && wifi != null) {
    		s = s + "if Wifi is " + onOrOff(wifi) + " ";
    		if (!mData.equals("") && mData != null) {
    			s = s + "and ";
    		}
    	}
    	if (!mData.equals("") && mData != null) {
    		if (!(!wifi.equals("") && wifi != null)) {
    			s = s + "if ";
    		}
    		s = s + "Mobile Data is " + onOrOff(mData) + " ";
    	}
    	
//    	//s = s.substring(0, 1).toUpperCase(Locale.getDefault()) + s.substring(1);
    	if (s.length() > 0) {
    	      s = s.substring(0, s.length()-1);
    	}

    	return s;
    }
    
    private String onOrOff(String bool) {
    	if (bool.equals("false") || bool == null) {
    		return "Off";
    	} else {
    		return "On";
    	}
    }
    
    public String actionsString() {
    	if (eventCategory == null || event == null) {
    		return null;
    	}
    	if (eventCategory.equals(Settings.RINGER)) {
    		return Settings.RINGER + ": " + RingerProfiles.intToRinger(Integer.parseInt(event));
    	} else if (eventCategory.equals(Settings.WIFI) || eventCategory.equals(Settings.MDATA)) {
    		return eventCategory + ": " + onOrOff(event);
    	}
    	return null;
    }
    
    public List<String> activeTriggerList() {
    	List<String> triggerList = new ArrayList<String>();
    	//Settings.TIME, Settings.DAY, Settings.LOCATION, Settings.WIFI, Settings.MDATA
    	if (this.getHour() != -1 && this.getMinute() != -1) {
    		triggerList.add(Settings.TIME + ": " + String.format("%02d",  this.getHour()) + ":" + String.format("%02d",  this.getMinute()));
    	}
    	if (this.getDay() != null && !this.getDay().equals("")) {
    		// TODO: FIX TIS FOR MULTIPLE DAYS
    		triggerList.add(Settings.DAY + ": " + intToDay(Integer.parseInt(getDay())));
    	}
    	
    	if (this.getLocation() != null && !this.getLocation().equals("")) {
    		String s = this.getLocation();

            try {
                int loc = Integer.parseInt(this.getLocation());
                for (UserLocation ul : UserLocationService.getInstance().getAllUserLocations()) {
                    if (ul.getId() == (loc)) {
                        s = ul.getName();
                        break;
                    }
                }
            } catch (NumberFormatException e) {

            }
            if (s.equals("-1")) {
                s = "Unknown";
            }
            
    		triggerList.add(Settings.LOCATION + ": " + s);
    	}
    	if (this.getWifi() != null && !this.getWifi().equals("")) {
    		triggerList.add(Settings.WIFI + ": " + onOrOff(this.getWifi()));
    	}
    	if (this.getmData() != null && !this.getmData().equals("")) {
    		triggerList.add(Settings.MDATA + ": " + onOrOff(this.getmData()));
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
