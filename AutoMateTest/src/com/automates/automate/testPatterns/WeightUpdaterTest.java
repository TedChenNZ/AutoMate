package com.automates.automate.testPatterns;

import com.automates.automate.pattern.Pattern;
import com.automates.automate.pattern.WeightUpdater;

import junit.framework.TestCase;

public class WeightUpdaterTest extends TestCase {

    public WeightUpdaterTest(String name) {
	super(name);
    }

    protected void setUp() throws Exception {
	super.setUp();
    }
    
    public Pattern p = new Pattern("WiFi", "Off", 45, 1406126739751L, 4, "Home", "false", "false", 0, 1, 0);
    public WeightUpdater updater = new WeightUpdater(p, 3);

    public void testSameDay(){
	
    }
}
