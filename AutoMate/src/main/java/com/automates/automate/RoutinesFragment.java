package com.automates.automate;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.automates.automate.adapter.EditMultiChoiceModeListener;
import com.automates.automate.adapter.RoutinesArrayAdapter;
import com.automates.automate.routines.Routine;
import com.automates.automate.routines.RoutineService;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoutinesFragment extends Fragment implements PropertyChangeListener {
    //	private TextView testText;

    private ListView routinesListView;
    private RoutinesArrayAdapter routinesAdapter;
    private List<Integer> selected = new ArrayList<Integer>();

    // action mode edit
    private ActionMode.Callback actionModeCallback;
    private ActionMode actionMode;
    private EditMultiChoiceModeListener modeListener;


    public RoutinesFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	View rootView = inflater.inflate(R.layout.fragment_routines, container, false);

	// Adapter
	routinesAdapter = new RoutinesArrayAdapter(this.getActivity().getApplicationContext(), R.layout.list_item_description, RoutineService.getInstance().getAllRoutines());

	// List View Initialize
	routinesListView = (ListView) rootView.findViewById(R.id.routinesListView);
	//        locationsListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	routinesListView.setAdapter(routinesAdapter);
	routinesListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

	// List View Mode Listener and CAB
	modeListener = new RoutinesModeListener(this.getActivity(), selected, routinesAdapter);
	routinesListView.setMultiChoiceModeListener(modeListener);
	actionModeCallback = new EditActionModeCallback(routinesListView, selected);


	routinesListView.setOnItemClickListener(new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> arg0, View view, int position,
		    long id) {
		if (selected.contains(position)) {
		    routinesListView.setItemChecked(position, false);
		    if (actionMode != null && selected.size() == 0) {
			actionMode.finish();
		    }
		} else {
		    // For multiple selections
		    if (actionMode == null || selected.size() == 0) {
			actionMode = getActivity().startActionMode(actionModeCallback);
		    }
		    routinesListView.setItemChecked(position, true);

		}

	    }
	});



	// Setting click event listener for the add button
	com.beardedhen.androidbootstrap.BootstrapButton addRoutine = (com.beardedhen.androidbootstrap.BootstrapButton) rootView.findViewById(R.id.addRoutine);
	addRoutine.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		Intent intent = new Intent(getActivity(), RoutineActivity.class);
		startActivityForResult(intent, 0);

		//                NotifyManager nM = new NotifyManager();
		//                nM.notification("subject", "body", 0);

	    }
	});

	//        updateCurrentLocation();
	// Listener
	PhoneService.getInstance().addChangeListener(this);

	return rootView;


    }
    @Override
    public void propertyChange(PropertyChangeEvent event) {

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
	// if nav drawer is opened, hide the action items
	menu.findItem(R.id.action_settings).setVisible(false);
	super.onPrepareOptionsMenu(menu);
    }

    @Override 
    public void onActivityResult(int requestCode, int resultCode, Intent data) {     
	super.onActivityResult(requestCode, resultCode, data); 
	switch(requestCode) { 
	case (0) : { 
	    if (resultCode == Activity.RESULT_OK) {
		routinesAdapter.notifyDataSetChanged();
		//	    	  	updateCurrentLocation();
	    } 
	    break; 
	} 
	} 
    }

    private class RoutinesModeListener extends EditMultiChoiceModeListener {

	private List<Integer> selected;
	private ArrayAdapter<?> adapter;
	private Activity activity;

	public RoutinesModeListener(Activity activity,
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
		    Intent intent = new Intent(activity, RoutineActivity.class);
		    Routine r = (Routine) adapter.getItem(selected.get(0));

		    intent.putExtra("routineID", r.getId());
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
			Routine r = (Routine) adapter.getItem(selected.get(i));
                RoutineService.getInstance().remove(r);
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

}
