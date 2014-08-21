package com.automates.automate.testPatterns;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.R;
import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import com.automates.automate.PhoneState;
import com.automates.automate.pattern.Pattern;
import com.automates.automate.pattern.PatternController;
import com.automates.automate.sqlite.PatternDB;
import com.automates.automate.sqlite.RoutineDB;
import com.automates.automate.sqlite.UserLocationsSQLiteDBManager;

public class PatternSimulator extends InstrumentationTestCase {
	private static final String TAG = "PatternSimulator";
	private static final String TEST_FILE_PREFIX = "test_";
	private RenamingDelegatingContext context;

	public PatternSimulator() {

	}



	@Override
	protected void setUp() throws Exception {
		super.setUp();
		context 
		= new RenamingDelegatingContext(getInstrumentation().getTargetContext(), TEST_FILE_PREFIX);
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
	private String readFile() {
		String s = "";
		
		try {
	        
	        InputStream in_s = this.getInstrumentation().getContext().getResources().openRawResource(com.automates.automate.test.R.raw.sim01);
	        byte[] b = new byte[in_s.available()];
	        in_s.read(b);
	        s = new String(b);
	    } catch (Exception e) {
	        // e.printStackTrace();
	    }
		return s;
	}
	private List<Pattern> getPatternSimulation() throws ParseException {
		String file[] = readFile().split("\n");
		//		String text = "04/07/2014 09:00:00 Wifi false Home";
		List<Pattern> list = new LinkedList<Pattern>();
		Pattern p;
		for (String s: file) {
			if (s.length() > 2) {
				String input[] = s.split(", ");
				if (input.length == 4) {
					SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					Date d = df.parse(input[0]);
					long epoch = d.getTime();
					
					p = new Pattern(input[1], input[2], d.getHours(), epoch, d.getDay(), input[3], "false", "false", 0, 1, 0);
					list.add(p);
				}
			}
		}

		return list;
	}

	public void testRoutineAmount() {
		int amount = PhoneState.getRoutineDb().getAllRoutines().size();
		assertEquals(3, amount);
	}

	public void testPatternAmount() {
		int amount = PhoneState.getPatternDb().getAllPatterns().size();
		assertEquals(6, amount);
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
