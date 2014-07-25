package com.automates.automate.routines;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.automates.automate.R;

public class RoutinesArrayAdapter extends ArrayAdapter<Routine> {
	private List<Routine> objects;
	private View v;

	public RoutinesArrayAdapter(Context context, int resource, List<Routine> objects) {
		super(context, resource, objects);
		this.objects = objects;
	}


	/*
	 * we are overriding the getView method here - this is what defines how each
	 * list item will look.
	 */
	@SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent){
		// assign the view we are converting to a local variable
		v = convertView;

		// first check to see if the view is null. if so, we have to inflate it.
		// to inflate it basically means to render, or show, the view.
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.list_item_location, null);
		}

		/*
		 * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 * 
		 * Therefore, i refers to the current Item object.
		 */
		Routine i = objects.get(position);

		if (i != null) {

			// This is how you obtain a reference to the TextViews.
			// These TextViews are created in the XML files we defined.

			TextView name = (TextView) v.findViewById(R.id.name);
			name.setText(i.getName());
			TextView details = (TextView) v.findViewById(R.id.details);
//			String d = i.getLocationName() + " (" + i.getRadius() + "m)";
			String d = "";
			d = d + ", " + i.getEvent();
			d = d + ", " + i.getEventCategory();
			d = d + ", " + i.getLocation();
			d = d + ", " + i.getWifi();
			d = d + ", " + i.getmData();
			d = d + ", " + i.getDay();
			d = d + ", " + i.getHour();
			d = d + ", " + i.getMinute();
			d = d + ", " + i.getStatusCode(); 
			
			
//			 Current crap
			
			
			
//			List<Routine> currentList = ((RoutinesList) PhoneState.getLocationsList()).checkLocation(PhoneState.getLocation());

//			if (currentList.contains(i)) {
//				name.setTextColor(v.getResources().getColor(R.color.active));
//			} else {
//				name.setTextColor(v.getResources().getColor(R.color.black));
//
//			}

			details.setText(d);
		}

		// the view must be returned to our activity
		return v;

	}
}
