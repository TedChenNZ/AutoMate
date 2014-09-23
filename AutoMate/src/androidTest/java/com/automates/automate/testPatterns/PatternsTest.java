package com.automates.automate.testPatterns;

import junit.framework.TestCase;
import com.automates.automate.model.Pattern;

public class PatternsTest extends TestCase {

    public PatternsTest(String name) {
	super(name);
    }

    protected void setUp() throws Exception {
	super.setUp();
    }
    
    public Pattern p = new Pattern("WiFi", "Off", 45, 1406126739751L, 4, "Home", "false", "false", 0, 1, 0);

    public void testCompare() {
	Pattern p2 = new Pattern("WiFi", "Off", 45, 12236739751L, 4, "abc", "true", "true", 1, 0, -1);
	assertTrue(p2.compare(p));
    }

    public void testCompareFail() {
	Pattern p2 = new Pattern("WiasdFi", "Off", 45, 12236739751L, 4, "abc", "true", "true", 1, 0, -1);
	assertFalse(p2.compare(p));
    }
    
    public void testCompareFail2() {
	Pattern p2 = new Pattern("WiFi", "Off", 345, 12236739751L, 4, "abc", "true", "true", 1, 0, -1);
	assertFalse(p2.compare(p));
    }
    
    public void testCompareFail3() {
	Pattern p2 = new Pattern("WiFi", "Ofsf", 45, 12236739751L, 4, "abc", "true", "true", 1, 0, -1);
	assertFalse(p2.compare(p));
    }
    
    public void testNullString() {
	Pattern p2 = new Pattern("WiFi", "Off", 45, 12236739751L, 4, null, "true", "true", 1, 0, -1);
	
	assertTrue(p2.getLocation().equals(""));
    }
    
}
