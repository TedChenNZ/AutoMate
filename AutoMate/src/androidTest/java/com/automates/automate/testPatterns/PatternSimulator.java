package com.automates.automate.testPatterns;
import android.test.InstrumentationTestCase;
import android.test.RenamingDelegatingContext;

import com.automates.automate.PhoneService;
import com.automates.automate.pattern.Pattern;
import com.automates.automate.pattern.PatternGenerator;
import com.automates.automate.pattern.PatternService;
import com.automates.automate.routines.RoutineService;
import com.automates.automate.sqlite.PatternDB;
import com.automates.automate.sqlite.RoutineDB;
import com.automates.automate.sqlite.UserLocationDB;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
		PhoneService.getInstance().update(context);
		context.deleteDatabase(TEST_FILE_PREFIX + UserLocationDB.DATABASE_NAME);
		context.deleteDatabase(TEST_FILE_PREFIX + PatternDB.DATABASE_NAME);
		context.deleteDatabase(TEST_FILE_PREFIX + RoutineDB.DATABASE_NAME);
		PhoneService.getInstance().update(context);
		List<Pattern> patternList = getPatternSimulation();
		for (Pattern p: patternList) {
			PatternGenerator pg = new PatternGenerator(p);
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
		int amount = RoutineService.getInstance().getAllRoutines().size();
		assertEquals(8, amount);
	}

	public void testPatternAmount() {
		int amount = PatternService.getInstance().getAllPatterns().size();
		assertEquals(21, amount);
	}

	public void testRoutine0Time() {
		int h = RoutineService.getInstance().getAllRoutines().get(0).getHour();
		assertEquals(9, h);
	}

}
