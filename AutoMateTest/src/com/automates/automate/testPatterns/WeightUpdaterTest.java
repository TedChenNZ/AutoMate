package com.automates.automate.testPatterns;

import java.lang.reflect.Field;

import junit.framework.TestCase;

import com.automates.automate.pattern.Pattern;
import com.automates.automate.pattern.WeightUpdater;

public class WeightUpdaterTest extends TestCase {


    //Thu Jul 24 2014 02:45:39
    public Pattern p = new Pattern("WiFi", "Off", 45, 1406126739751L, 4, "Home", "false", "false", 0, 1, 0);
    //Thu Jul 24 2014 14:45:39  -- 12 hours
    public Pattern twelveP = new Pattern("WiFi", "Off", 45, 1406169939000L, 4, "Home", "false", "false", 0, 1, 0);
    //Thu Jul 25 2014 02:45:39 -- 24 hours/1day
    public Pattern oneDayP = new Pattern("WiFi", "Off", 45, 1406213139000L, 4, "Home", "false", "false", 0, 1, 0);
    //Thu Jul 31 2014 02:45:39 -- 1 week
    public Pattern oneWeekP = new Pattern("WiFi", "Off", 45, 1406731539000L, 4, "Home", "false", "false", 0, 1, 0);



    public WeightUpdaterTest(String name) {
	super(name);
    }

    protected void setUp() throws Exception {
	super.setUp();
    }



    public void testSameDay() throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException{
	WeightUpdater updater = new WeightUpdater(p, 3);
	Class<?> updaterClass = updater.getClass(); 
	
	final Field oldP = updaterClass.getDeclaredField("oldP");
	oldP.setAccessible(true);
	oldP.set(updater, twelveP);
	
	final Field oldWeight = updaterClass.getDeclaredField("oldWeight");
	oldWeight.setAccessible(true);
	oldWeight.set(updater, twelveP.getWeight());
	
	
	final Field oldWeekWeight = updaterClass.getDeclaredField("oldWeekWeight");
	oldWeekWeight.setAccessible(true);
	oldWeekWeight.set(updater, twelveP.getWeekWeight());
	
	final Field timeDiff = updaterClass.getDeclaredField("timeDiff");
	timeDiff.setAccessible(true);
	timeDiff.set(updater, p.getActualTime() - twelveP.getActualTime());
	
	Pattern newP = new Pattern(); 
	newP = updater.updatePattern();
	assertEquals(1.0, newP.getWeight());
    }
    
    public void testOneDay() throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException{
	WeightUpdater updater = new WeightUpdater(p, 3);
	Class<?> updaterClass = updater.getClass(); 
	
	final Field oldP = updaterClass.getDeclaredField("oldP");
	oldP.setAccessible(true);
	oldP.set(updater, oneDayP);
	
	final Field oldWeight = updaterClass.getDeclaredField("oldWeight");
	oldWeight.setAccessible(true);
	oldWeight.set(updater, oneDayP.getWeight());
	
	
	final Field oldWeekWeight = updaterClass.getDeclaredField("oldWeekWeight");
	oldWeekWeight.setAccessible(true);
	oldWeekWeight.set(updater, oneDayP.getWeekWeight());
	
	final Field timeDiff = updaterClass.getDeclaredField("timeDiff");
	timeDiff.setAccessible(true);
	timeDiff.set(updater, p.getActualTime() - oneDayP.getActualTime());
	
	Pattern newP = new Pattern(); 
	newP = updater.updatePattern();
	assertEquals(2.0, newP.getWeight());
    }
    
    public void testOneWeek() throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException{
	WeightUpdater updater = new WeightUpdater(p, 3);
	Class<?> updaterClass = updater.getClass(); 
	
	final Field oldP = updaterClass.getDeclaredField("oldP");
	oldP.setAccessible(true);
	oldP.set(updater, oneWeekP);
	
	final Field oldWeight = updaterClass.getDeclaredField("oldWeight");
	oldWeight.setAccessible(true);
	oldWeight.set(updater, oneWeekP.getWeight());
	
	
	final Field oldWeekWeight = updaterClass.getDeclaredField("oldWeekWeight");
	oldWeekWeight.setAccessible(true);
	oldWeekWeight.set(updater, oneWeekP.getWeekWeight());
	
	final Field timeDiff = updaterClass.getDeclaredField("timeDiff");
	timeDiff.setAccessible(true);
	timeDiff.set(updater, p.getActualTime() - twelveP.getActualTime());
	
	Pattern newP = new Pattern(); 
	newP = updater.updatePattern();
	assertEquals(2.0, newP.getWeight());
    }

}
