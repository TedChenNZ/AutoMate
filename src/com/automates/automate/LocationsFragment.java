package com.automates.automate;

import java.util.ArrayList;
import java.util.List;

import com.automates.automate.locations.UserLocation;
import com.automates.automate.locations.UserLocationsArrayAdapter;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class LocationsFragment extends Fragment {
	private static final String TAG = "LocationsFragment";
	private List<UserLocation> locationsList;
	private ListView locationsListView;
	private UserLocationsArrayAdapter locationsAdaptor;
	
	
	public LocationsFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_locations, container, false);
        
        // List
        locationsList = PhoneState.getLocationList();
        locationsListView = (ListView) rootView.findViewById(R.id.locationsListView);
        locationsAdaptor = new UserLocationsArrayAdapter(this.getActivity().getApplicationContext(), R.layout.list_item_location, locationsList);
        locationsListView.setAdapter(locationsAdaptor);
        

        // Setting click event listener for the add button
        Button addLocation = (Button) rootView.findViewById(R.id.addLocation);
        addLocation.setOnClickListener(new OnClickListener() {
        	
            @Override
            public void onClick(View v) {
            	Intent intent = new Intent(getActivity(), LocationActivity.class);
            	startActivityForResult(intent, 0);
            }
		});
        
        return rootView;
    }
	
	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) {     
	  super.onActivityResult(requestCode, resultCode, data); 
	  
	  switch(requestCode) { 
	    case (0) : { 
	      if (resultCode == Activity.RESULT_OK) {
	    	  	Log.d(TAG, "OK");
	    	  	locationsList = PhoneState.getLocationList();
	    	  	locationsAdaptor.notifyDataSetChanged();
		      } 
	      break; 
	    } 
	  } 
	}
	
}
