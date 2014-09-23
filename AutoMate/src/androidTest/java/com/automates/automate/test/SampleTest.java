package com.automates.automate.test;

import com.automates.automate.view.activities.MainActivity;

import android.test.ActivityInstrumentationTestCase2;

public class SampleTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public SampleTest(String name) {
	super(MainActivity.class);
    }

    protected void setUp() throws Exception {
	super.setUp();
	
	MainActivity mainActivity = getActivity();
    }

}
