package com.automates.automate.adapter;

import java.util.List;

import com.automates.automate.PhoneState;
import com.automates.automate.R;
import com.automates.automate.locations.UserLocation;
import com.automates.automate.locations.UserLocationsList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class UserLocationsArrayAdapter extends ArrayAdapter<UserLocation> {

	private List<UserLocation> objects;
	private View v;

	public UserLocationsArrayAdapter(Context context, int resource, List<UserLocation> objects) {
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
			v = inflater.inflate(R.layout.list_item_description, null);
		}

		/*
		 * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 * 
		 * Therefore, i refers to the current Item object.
		 */
		UserLocation i = objects.get(position);

		if (i != null) {

			// This is how you obtain a reference to the TextViews.
			// These TextViews are created in the XML files we defined.

			TextView name = (TextView) v.findViewById(R.id.name);
			name.setText(i.getName());
			TextView details = (TextView) v.findViewById(R.id.details);
			String d = i.getLocationName() + " (" + i.getRadius() + "m)";
			details.setText(d);
			
			List<UserLocation> currentList = ((UserLocationsList) PhoneState.getLocationsList()).checkLocation(PhoneState.getLocation());

			if (currentList.contains(i)) {
				name.setTextColor(v.getResources().getColor(R.color.active));
			} else {
				name.setTextColor(v.getResources().getColor(R.color.black));

			}

			
		}

		// the view must be returned to our activity
		return v;

	}

}