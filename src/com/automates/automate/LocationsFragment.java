package com.automates.automate;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.automates.automate.locations.EditMultiChoiceModeListener;
import com.automates.automate.locations.UserLocation;
import com.automates.automate.locations.UserLocationsArrayAdapter;
import com.automates.automate.locations.UserLocationsList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class LocationsFragment extends Fragment implements PropertyChangeListener {
//	private static final String TAG = "LocationsFragment";
	private ListView locationsListView;
	private UserLocationsArrayAdapter locationsAdapter;
	private List<Integer> selected = new ArrayList<Integer>();
	
    // action mode edit
    private ActionMode.Callback actionModeCallback;
    private ActionMode actionMode;
    private EditMultiChoiceModeListener modeListener;
    
    
	public LocationsFragment(){}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_locations, container, false);
        
        // Adapter
        locationsAdapter = new UserLocationsArrayAdapter(this.getActivity().getApplicationContext(), R.layout.list_item_location, PhoneState.getLocationsList());
        
        // Locations List View Initialize
        locationsListView = (ListView) rootView.findViewById(R.id.locationsListView);
//        locationsListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        locationsListView.setAdapter(locationsAdapter);
        locationsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        
        // Locations List View Mode Listener and CAB
        modeListener = new LocationsModeListener(this.getActivity(), selected, locationsAdapter);
        locationsListView.setMultiChoiceModeListener(modeListener);
        actionModeCallback = new EditActionModeCallback(locationsListView, selected);
        
        
        locationsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				if (selected.contains(position)) {
					locationsListView.setItemChecked(position, false);
					if (actionMode != null && selected.size() == 0) {
						actionMode.finish();
					}
				} else {
					// For only 1 selection
//					if (actionMode != null && selected.size() == 0) {
//						actionMode.finish();
//					}
					
					// For multiple selections
					if (actionMode == null || selected.size() == 0) {
						actionMode = getActivity().startActionMode(actionModeCallback);
					}
					locationsListView.setItemChecked(position, true);
					
				}

			}
        });
        
        
        
        // Setting click event listener for the add button
        Button addLocation = (Button) rootView.findViewById(R.id.addLocation);
        addLocation.setOnClickListener(new OnClickListener() {
        	
            @Override
            public void onClick(View v) {
            	Intent intent = new Intent(getActivity(), LocationActivity.class);
            	startActivityForResult(intent, 0);
            }
		});
        
        updateCurrentLocation();
        // Listener
        PhoneState.getInstance().addChangeListener(this);
        return rootView;
    }
	
	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) {     
	  super.onActivityResult(requestCode, resultCode, data); 
	  switch(requestCode) { 
	    case (0) : { 
	      if (resultCode == Activity.RESULT_OK) {
	    	  	locationsAdapter.notifyDataSetChanged();
	    	  	updateCurrentLocation();
		      } 
	      break; 
	    } 
	  } 
	}
	
	

	private class LocationsModeListener extends EditMultiChoiceModeListener {
		
		private List<Integer> selected;
		private ArrayAdapter<?> adapter;
		private Activity activity;
		
		public LocationsModeListener(Activity activity,
				List<Integer> selected, ArrayAdapter<?> adapter) {
			super(selected);
			this.activity = activity;
			this.selected = selected;
			this.adapter = adapter;
		}
		
		
		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
				case R.id.action_edit:
					if (selected != null && selected.size() == 1) {
						Intent intent = new Intent(activity, LocationActivity.class);
						intent.putExtra("EditItem", selected.get(0));
		            	activity.startActivityForResult(intent, 0);
					}
					adapter.notifyDataSetChanged();
					mode.finish();
					return true;
				case R.id.action_discard:
					if (selected != null) {
						Collections.sort(selected);
						Collections.reverse(selected);
						for (int i = 0; i < selected.size(); i++) {
							UserLocation ul = (UserLocation) adapter.getItem(selected.get(i));
							PhoneState.getLocationsList().remove(ul);
						}
						adapter.notifyDataSetChanged();
						mode.finish();
					}
					return true;
				default:
					return false;
					
			}
			
		}
	}


	@Override
	public void propertyChange(PropertyChangeEvent event) {
		updateCurrentLocation();
	}
	
	
	private void updateCurrentLocation() {
		List<UserLocation> currentList = ((UserLocationsList) PhoneState.getLocationsList()).checkLocation(PhoneState.getLocation());
		List<UserLocation> list = PhoneState.getLocationsList();
		int index = 0;
		for (UserLocation ul : list) {
			for (UserLocation cul: currentList) {
				View v = locationsListView.getChildAt(index - locationsListView.getFirstVisiblePosition());
				if(v != null) {
					TextView name = (TextView) v.findViewById(R.id.name);
					if (ul.getId() == cul.getId()) {
						name.setTextColor(getResources().getColor(R.color.active));
					} else {
						name.setTextColor(getResources().getColor(R.color.black));
					}
				}
				index = index + 1;
			}
		}
	}
	
}
