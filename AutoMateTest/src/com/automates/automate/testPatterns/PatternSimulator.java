package com.automates.automate.testPatterns;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.IsolatedContext;
import android.test.RenamingDelegatingContext;

import com.automates.automate.pattern.Pattern;
import com.automates.automate.pattern.PatternControl;
import com.automates.automate.pattern.PatternController;
import com.automates.automate.sqlite.PatternDB;

public class PatternSimulator extends AndroidTestCase {
	private PatternDB patternDB;
    private static final String TEST_FILE_PREFIX = "test_";
    
	public PatternSimulator() {
		
	}
	


	@Override
	protected void setUp() throws Exception {
	    super.setUp();
	    RenamingDelegatingContext context 
	        = new RenamingDelegatingContext(getContext(), TEST_FILE_PREFIX);
	    patternDB = new PatternDB(context);
	    List<Pattern> patternList = getPatternSimulation();
	    for (Pattern p: patternList) {
		    PatternController pg = new PatternController(p);
		    pg.setWeightsandStatusCode();
			pg.updateDatabase();
	    }
	}
	
	private List<Pattern> getPatternSimulation() {
		List<Pattern> list = new LinkedList<Pattern>();
	    Pattern p;
	    
	    
	    // Mon, 04 Aug 2014 09:00:00 GMT
	    p = new Pattern("WiFi", "Off", 45, 1407142800000L, 4, "Home", "false", "false", 0, 1, 0);
	    list.add(p);
	    // Mon, 04 Aug 2014 10:00:00 GMT
	    p = new Pattern("WiFi", "On", 45, 1407664800000L, 4, "Work", "false", "false", 0, 1, 0);
	    list.add(p);

	    
	    // Tue, 05 Aug 2014 09:00:00 GMT
	    p = new Pattern("WiFi", "Off", 45, 1407229200000L, 4, "Home", "false", "false", 0, 1, 0);
	    list.add(p);
	    // Tue, 05 Aug 2014 10:05:36 GMT
	    p = new Pattern("WiFi", "On", 45, 1407233136000L, 4, "Work", "false", "false", 0, 1, 0);
	    list.add(p);
	    
	    // Wed, 06 Aug 2014 09:00:00 GMT
	    p = new Pattern("WiFi", "Off", 45, 1407315600000L, 4, "Home", "false", "false", 0, 1, 0);
	    list.add(p);
	    
	    
	    // Thur, 07 Aug 2014 09:00:00 GMT
	    p = new Pattern("WiFi", "Off", 45, 1407402000000L, 4, "Home", "false", "false", 0, 1, 0);
	    list.add(p);
	    
	    
	    // Fri, 08 Aug 2014 09:00:00 GMT
	    p = new Pattern("WiFi", "Off", 45, 1407488400000L, 4, "Home", "false", "false", 0, 1, 0);
	    list.add(p);
	    
	    
	    // Sat, 09 Aug 2014 09:00:00 GMT
	    p = new Pattern("WiFi", "Off", 45, 1407574800000L, 4, "Home", "false", "false", 0, 1, 0);
	    list.add(p);
	    
	    
	    // Sun, 10 Aug 2014 09:00:00 GMT
	    p = new Pattern("WiFi", "Off", 45, 1407661200000L, 4, "Home", "false", "false", 0, 1, 0);
	    list.add(p);
		
		return list;
	}

}
