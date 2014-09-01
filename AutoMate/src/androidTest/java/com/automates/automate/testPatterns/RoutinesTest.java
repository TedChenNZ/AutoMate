package com.automates.automate.testPatterns;

import java.lang.reflect.InvocationTargetException;

import junit.framework.TestCase;
import android.text.format.Time;

import com.automates.automate.PhoneService;
import com.automates.automate.pattern.Pattern;
import com.automates.automate.pattern.StatusCode;
import com.automates.automate.routines.Routine;

public class RoutinesTest extends TestCase {

    public RoutinesTest(String name) {
	super(name);
    }

    protected void setUp() throws Exception {
	super.setUp();
    }
    
    private boolean checkPhoneConditions(Routine r){
	boolean conditions = true;

	Time time = new Time();
	time.setToNow();

	if (!r.getDay().equals(StatusCode.EMPTY) && r.getDay().indexOf(time.weekDay) == -1){conditions = false;};
	if (r.getHour() != (StatusCode.DECLINED) && r.getHour() != time.hour){conditions = false;};
	if (r.getMinute() != (StatusCode.DECLINED) && r.getMinute() != time.minute){conditions = false;};
	if (!r.getLocation().equals(StatusCode.EMPTY) && !r.getLocation().equals(PhoneService.getSetLocation())){conditions = false;};
	if (!r.getmData().equals(StatusCode.EMPTY) && !r.getmData().equals(Boolean.toString(PhoneService.isDataEnabled()))){conditions = false;};
	if (!r.getWifi().equals(StatusCode.EMPTY) && !r.getWifi().equals(PhoneService.getWifiBSSID())){conditions = false;};


	return conditions;
    }

    public Pattern p = new Pattern("WiFi", "Off", 45, 1406126739751L, 4, "Home", "false", "false", 0, 1, 0);
    public Routine r = new Routine(1, "rName1", "WiFi", "Off", 13, 22, "4", "Home", "false", "false", 1);
//    
//    public Routine offAtNight = new Routine(1, "Sound off at night", "Ringer", "0", 22, 0, "", "", "", "", 1);
//    public Routine WifiOffAtNight = new Routine(1, "Wifi off at night", "Wifi", "false", 22, 0, "", "", "", "", 1);
//    public Routine onInMorning = new Routine(1, "Sound on in morning", "Ringer", "4", 7, 0, "", "", "", "", 1);
//    public Routine WifiOnInMorning = new Routine(1, "Wifi on at night", "Wifi", "on", 7, 0, "", "", "", "", 1);
//    public Routine WifiOnAtHome = new Routine(1, "Wifi on at home", "Wifi", "on", -1, -1, "", "Home", "", "", 1);
    


    public void testPhoneConditions() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
	assertTrue(checkPhoneConditions(r));
    }

    
}
