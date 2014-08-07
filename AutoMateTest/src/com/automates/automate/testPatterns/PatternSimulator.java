package com.automates.automate.testPatterns;
import java.util.LinkedList;
import java.util.List;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import com.automates.automate.PhoneState;
import com.automates.automate.pattern.Pattern;
import com.automates.automate.pattern.PatternController;
import com.automates.automate.sqlite.PatternDB;
import com.automates.automate.sqlite.RoutineDB;
import com.automates.automate.sqlite.UserLocationsSQLiteDBManager;

public class PatternSimulator extends AndroidTestCase {

	private static final String TEST_FILE_PREFIX = "test_";
	private RenamingDelegatingContext context;

	public PatternSimulator() {

	}



	@Override
	protected void setUp() throws Exception {
		super.setUp();
		context 
		= new RenamingDelegatingContext(getContext(), TEST_FILE_PREFIX);
		PhoneState.update(context);
		context.deleteDatabase(TEST_FILE_PREFIX + UserLocationsSQLiteDBManager.DATABASE_NAME);
		context.deleteDatabase(TEST_FILE_PREFIX + PatternDB.DATABASE_NAME);
		context.deleteDatabase(TEST_FILE_PREFIX + RoutineDB.DATABASE_NAME);
		PhoneState.update(context);
		List<Pattern> patternList = getPatternSimulation();
		for (Pattern p: patternList) {
			PatternController pg = new PatternController(p);
			pg.updateDatabase();
		}
	}

	private List<Pattern> getPatternSimulation() {
		//		String text = "04/07/2014 09:00:00 Wifi false Home";
		List<Pattern> list = new LinkedList<Pattern>();
		Pattern p;


		// Mon, 04 Aug 2014 09:00:00 GMT+12
		p = new Pattern("Wifi", "false", 9, 1407099600000L, 1, "Home", "false", "false", 0, 1, 0);
		list.add(p);
		// Mon, 04 Aug 2014 10:02:30 GMT+12
		p = new Pattern("Wifi", "true", 10, 1407103200000L, 1, "Work", "false", "false", 0, 1, 0);
		list.add(p);
		p = new Pattern("Ringer", "1", 10, 1407103200000L, 1, "Work", "false", "false", 0, 1, 0);
		list.add(p);
		// Mon, 04 Aug 2014 17:02:03 GMT+12
		p = new Pattern("Wifi", "false", 17, 1407128400000L, 1, "", "false", "false", 0, 1, 0);
		list.add(p);
		p = new Pattern("Ringer", "3", 17, 1407128400000L, 1, "Home", "false", "false", 0, 1, 0);
		list.add(p);

		// Tue, 05 Aug 2014 09:00:00 GMT+12
		p = new Pattern("Wifi", "false", 9, 1407186000000L, 2, "Home", "false", "false", 0, 1, 0);
		list.add(p);
		
		// Tue, 05 Aug 2014 10:05:36 GMT+12
		p = new Pattern("Wifi", "true", 10, 1407189600000L, 2, "Work", "false", "false", 0, 1, 0);
		list.add(p);
		p = new Pattern("Ringer", "1", 10, 1407189600000L, 2, "Work", "false", "false", 0, 1, 0);
		list.add(p);


		// Wed, 06 Aug 2014 09:00:00 GMT+12
		p = new Pattern("Wifi", "false", 9, 1407272400000L, 3, "Home", "false", "false", 0, 1, 0);
		list.add(p);


		// Thur, 07 Aug 2014 09:00:00 GMT+12
		p = new Pattern("Wifi", "false", 9, 1407358800000L, 4, "Home", "false", "false", 0, 1, 0);
		list.add(p);


		// Fri, 08 Aug 2014 09:00:00 GMT+12
		p = new Pattern("Wifi", "false", 9, 1407445200000L, 5, "Home", "false", "false", 0, 1, 0);
		list.add(p);


		// Sat, 09 Aug 2014 09:00:00 GMT+12
		p = new Pattern("Wifi", "false", 9, 1407531600000L, 6, "Home", "false", "false", 0, 1, 0);
		list.add(p);


		// Sun, 10 Aug 2014 09:00:00 GMT+12
		p = new Pattern("Wifi", "false", 9, 1407618000000L, 0, "Home", "false", "false", 0, 1, 0);
		list.add(p);

		return list;
	}

	public void testRoutineAmount() {
		int amount = PhoneState.getRoutineDb().getAllRoutines().size();
		assertEquals(3, amount);
	}

	public void testPatternAmount() {
		int amount = PhoneState.getPatternDb().getAllPatterns().size();
		assertEquals(5, amount);
	}

	public void testPattern0() {
		double weight = PhoneState.getPatternDb().getAllPatterns().get(0).getWeight();
		assertTrue(2.0 <= weight);
	}

	public void testPattern1() {
		double weight = PhoneState.getPatternDb().getAllPatterns().get(1).getWeight();
		assertTrue(2.0 >= weight);
	}

	public void testRoutine0Time() {
		int h = PhoneState.getRoutineDb().getAllRoutines().get(0).getHour();
		assertEquals(9, h);
	}

}
