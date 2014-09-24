package com.automates.automate.testPatterns;
import android.test.InstrumentationTestCase;
import android.test.RenamingDelegatingContext;

import com.automates.automate.service.PhoneService;
import com.automates.automate.model.Pattern;
import com.automates.automate.service.pattern.PatternGenerator;
import com.automates.automate.service.model.PatternService;
import com.automates.automate.service.model.RoutineService;
import com.automates.automate.sqlite.PatternDB;
import com.automates.automate.sqlite.RoutineDB;
import com.automates.automate.sqlite.UserLocationDB;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Testing class to simulate usage of the application. Used to determine the accuracy of learning.
 */
public class PatternSimulator extends InstrumentationTestCase {
	private static final String TAG = "PatternSimulator";
	private static final String TEST_FILE_PREFIX = "test_";
	private RenamingDelegatingContext context;

	public PatternSimulator() {

	}


    /**
     * Setup the testing by creating or emptying test databases
     * @throws Exception
     */
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

    /**
     * Reads the simulation file
     * @return Returns the simulation file as a string
     */
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

    /**
     * Create a list of patterns to be inputted into the simulator
     * @return List of patterns
     * @throws ParseException
     */
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

    /**
     * Test the amount of Routines
     */
	public void testRoutineAmount() {
		int amount = RoutineService.getInstance().getAllRoutines().size();
		assertEquals(8, amount);
	}

    /**
     * Test the amount of Patterns
     */
	public void testPatternAmount() {
		int amount = PatternService.getInstance().getAllPatterns().size();
		assertEquals(21, amount);
	}

}
