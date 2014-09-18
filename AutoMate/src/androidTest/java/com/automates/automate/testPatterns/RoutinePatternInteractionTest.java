package com.automates.automate.testPatterns;

import android.test.InstrumentationTestCase;
import android.test.RenamingDelegatingContext;

import com.automates.automate.LoggerService;
import com.automates.automate.PhoneService;
import com.automates.automate.routines.Routine;
import com.automates.automate.routines.RoutineService;
import com.automates.automate.settings.Wifi;
import com.automates.automate.sqlite.PatternDB;
import com.automates.automate.sqlite.RoutineDB;
import com.automates.automate.sqlite.UserLocationDB;

public class RoutinePatternInteractionTest extends InstrumentationTestCase {

	private static final String TAG = "PatternSimulator";
	private static final String TEST_FILE_PREFIX = "test_";
	private RenamingDelegatingContext context;
	
	public RoutinePatternInteractionTest() {}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		context 
		= new RenamingDelegatingContext(getInstrumentation().getTargetContext(), TEST_FILE_PREFIX);
		Wifi.setWifiEnabled(context, true);
		PhoneService.getInstance().update(context);
		context.deleteDatabase(TEST_FILE_PREFIX + UserLocationDB.DATABASE_NAME);
		context.deleteDatabase(TEST_FILE_PREFIX + PatternDB.DATABASE_NAME);
		context.deleteDatabase(TEST_FILE_PREFIX + RoutineDB.DATABASE_NAME);
		
		
		Routine r = new Routine();
		r.setEventCategory(com.automates.automate.settings.Settings.WIFI);
		r.setEvent("true");
		r.setWifi("false");
		RoutineService.getInstance().add(r);
	}
	
	public void testLoggerGetAppliedRoutines() {
		Wifi.setWifiEnabled(context, false);
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		int size = LoggerService.getInstance().getAppliedRoutines().size();
		assertEquals(1, size);

		}
	
	

}
