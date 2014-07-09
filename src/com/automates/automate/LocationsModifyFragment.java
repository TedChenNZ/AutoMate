package com.automates.automate;

import com.google.android.gms.maps.MapView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LocationsModifyFragment extends Fragment {
	MapView m;
	
	public LocationsModifyFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_locations_modify, container, false);
        m = (MapView) rootView.findViewById(R.id.mapView);
        m.onCreate(savedInstanceState);
        return rootView;
    }
	@Override
	public void onResume() {
		super.onResume();
		m.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		m.onPause();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		m.onDestroy();
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		m.onLowMemory();
	}
}
