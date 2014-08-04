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
		    PatternControl pg = new PatternController(p);
			pg.updateDatabase();
	    }
	}
	
	private List<Pattern> getPatternSimulation() {
		List<Pattern> list = new LinkedList<Pattern>();
	    Pattern p;
	    p = new Pattern("WiFi", "Off", 45, 1406126739751L, 4, "Home", "false", "false", 0, 1, 0);
	    list.add(p);
		
		
		return list;
	}

}
