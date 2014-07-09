package com.automates.automate;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LocationsFragment extends Fragment {
	public LocationsFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_locations, container, false);
         
    	Button addButton = (Button) rootView.findViewById(R.id.addButton);
    	addButton.setOnClickListener(new ButtonEvent());
    	
    	
    	
        return rootView;
    }
	
	private class ButtonEvent implements OnClickListener {
		@Override
		public void onClick(View v) {
			Log.d("LocationsFragment", "click");
			((MainActivity)getActivity()).displayOtherView(6);
		}
	}
}
