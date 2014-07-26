package com.automates.automate.testPatterns;

import java.lang.reflect.Field;

import junit.framework.TestCase;

import com.automates.automate.pattern.Pattern;
import com.automates.automate.pattern.WeightUpdater;

//COMMENT OUT oldPset(id); in the WeightUpdater constructor to run this test properly.

public class WeightUpdaterTest extends TestCase {


    //Thu Jul 24 2014 02:45:39
    public Pattern p = new Pattern("WiFi", "Off", 45, 1406126739751L, 4, "Home", "false", "false", 0, 1, 0);
    //Thu Jul 24 2014 14:45:39  -- 12 hours
    public Pattern twelveP = new Pattern("WiFi", "Off", 45, 1406169939000L, 4, "Home", "false", "false", 0, 1, 0);
    //Fri Jul 25 2014 02:45:39 -- 24 hours/1day
    public Pattern oneDayP = new Pattern("WiFi", "Off", 45, 1406213139000L, 5, "Home", "false", "false", 0, 1, 0);
    //Thu Jul 31 2014 02:45:39 -- 1 week
    public Pattern oneWeekP = new Pattern("WiFi", "Off", 45, 1406731539000L, 4, "Home", "false", "false", 0, 1, 0);
    //Tue Jul 29 2014 18:45:39 -- 5 days
    public Pattern fiveDaysP = new Pattern("WiFi", "Off", 45, 1406556939000L, 2, "Home", "false", "false", 0, 1, 0);
    //Thu Aug 7 2014 2014 18:45:39 -- 2 weeks
    public Pattern twoWeeksP = new Pattern("WiFi", "Off", 45, 1407334539000L, 4, "Home", "false", "false", 0, 1, 0);


    public WeightUpdaterTest(String name) {
	super(name);
    }

    protected void setUp() throws Exception {
	super.setUp();
    }



    public void testSameDay() throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException{
	WeightUpdater updater = new WeightUpdater(twelveP, 3);
	Class<?> updaterClass = updater.getClass(); 

	final Field oldP = updaterClass.getDeclaredField("oldP");
	oldP.setAccessible(true);
	oldP.set(updater, p);

	final Field oldWeight = updaterClass.getDeclaredField("oldWeight");
	oldWeight.setAccessible(true);
	oldWeight.set(updater, p.getWeight());


	final Field oldWeekWeight = updaterClass.getDeclaredField("oldWeekWeight");
	oldWeekWeight.setAccessible(true);
	oldWeekWeight.set(updater, p.getWeekWeight());

	final Field timeDiff = updaterClass.getDeclaredField("timeDiff");
	timeDiff.setAccessible(true);
	timeDiff.set(updater, twelveP.getActualTime() - p.getActualTime());

	Pattern newP = new Pattern(); 
	newP = updater.updatePattern();
	assertEquals(1.0, newP.getWeight());
    }

    public void testOneDay() throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException{
	WeightUpdater updater = new WeightUpdater(oneDayP, 3);
	Class<?> updaterClass = updater.getClass(); 

	final Field oldP = updaterClass.getDeclaredField("oldP");
	oldP.setAccessible(true);
	oldP.set(updater, p);

	final Field oldWeight = updaterClass.getDeclaredField("oldWeight");
	oldWeight.setAccessible(true);
	oldWeight.set(updater, p.getWeight());


	final Field oldWeekWeight = updaterClass.getDeclaredField("oldWeekWeight");
	oldWeekWeight.setAccessible(true);
	oldWeekWeight.set(updater, p.getWeekWeight());

	final Field timeDiff = updaterClass.getDeclaredField("timeDiff");
	timeDiff.setAccessible(true);
	timeDiff.set(updater, oneDayP.getActualTime() - p.getActualTime());

	Pattern newP = new Pattern(); 
	newP = updater.updatePattern();
	assertEquals(2.0, newP.getWeight());
    }

    public void testOneWeek() throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException{
	WeightUpdater updater = new WeightUpdater(oneWeekP, 3);
	Class<?> updaterClass = updater.getClass(); 

	final Field oldP = updaterClass.getDeclaredField("oldP");
	oldP.setAccessible(true);
	oldP.set(updater, p);

	final Field oldWeight = updaterClass.getDeclaredField("oldWeight");
	oldWeight.setAccessible(true);
	oldWeight.set(updater, p.getWeight());


	final Field oldWeekWeight = updaterClass.getDeclaredField("oldWeekWeight");
	oldWeekWeight.setAccessible(true);
	oldWeekWeight.set(updater, p.getWeekWeight());

	final Field timeDiff = updaterClass.getDeclaredField("timeDiff");
	timeDiff.setAccessible(true);
	timeDiff.set(updater,  oneWeekP.getActualTime() - p.getActualTime());

	Pattern newP = new Pattern(); 
	newP = updater.updatePattern();
	assertEquals(2.0, newP.getWeight());
    }

    public void testFiveDays() throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException{
	WeightUpdater updater = new WeightUpdater(fiveDaysP, 3);
	Class<?> updaterClass = updater.getClass(); 

	final Field oldP = updaterClass.getDeclaredField("oldP");
	oldP.setAccessible(true);
	oldP.set(updater, p);

	final Field oldWeight = updaterClass.getDeclaredField("oldWeight");
	oldWeight.setAccessible(true);
	oldWeight.set(updater, p.getWeight());


	final Field oldWeekWeight = updaterClass.getDeclaredField("oldWeekWeight");
	oldWeekWeight.setAccessible(true);
	oldWeekWeight.set(updater, p.getWeekWeight());

	final Field timeDiff = updaterClass.getDeclaredField("timeDiff");
	timeDiff.setAccessible(true);
	timeDiff.set(updater,  fiveDaysP.getActualTime() - p.getActualTime());

	Pattern newP = new Pattern(); 
	newP = updater.updatePattern();

	assertEquals(1 + (1.0/5.0), newP.getWeight());
    }

    public void testTwoWeeks() throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException{
	WeightUpdater updater = new WeightUpdater(twoWeeksP, 3);
	Class<?> updaterClass = updater.getClass(); 

	final Field oldP = updaterClass.getDeclaredField("oldP");
	oldP.setAccessible(true);
	oldP.set(updater, p);

	final Field oldWeight = updaterClass.getDeclaredField("oldWeight");
	oldWeight.setAccessible(true);
	oldWeight.set(updater, p.getWeight());


	final Field oldWeekWeight = updaterClass.getDeclaredField("oldWeekWeight");
	oldWeekWeight.setAccessible(true);
	oldWeekWeight.set(updater, p.getWeekWeight());

	final Field timeDiff = updaterClass.getDeclaredField("timeDiff");
	timeDiff.setAccessible(true);
	timeDiff.set(updater, twoWeeksP.getActualTime() - p.getActualTime());

	Pattern newP = new Pattern(); 
	newP = updater.updatePattern();

	assertEquals(1 + (1.0/2.0), newP.getWeight());
    }
    
}
