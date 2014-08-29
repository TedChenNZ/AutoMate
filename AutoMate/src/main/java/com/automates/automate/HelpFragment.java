package com.automates.automate;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

public class HelpFragment extends Fragment {

    public HelpFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

	View rootView = inflater.inflate(R.layout.fragment_help, container, false);

	return rootView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
	// if nav drawer is opened, hide the action items
	menu.findItem(R.id.action_settings).setVisible(false);
	super.onPrepareOptionsMenu(menu);
    }
}
