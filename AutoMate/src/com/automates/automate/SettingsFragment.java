package com.automates.automate;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.Menu;

public class SettingsFragment extends PreferenceFragment  {

	public SettingsFragment(){}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);
	}

	    @Override
	    public void onPrepareOptionsMenu(Menu menu) {
	        // if nav drawer is opened, hide the action items
	        menu.findItem(R.id.action_settings).setVisible(false);
	        super.onPrepareOptionsMenu(menu);
	    }
}
