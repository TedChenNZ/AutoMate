package com.automates.automate.view.list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.automates.automate.R;

import java.util.List;

/**
 * A simple list adapter
 */
public class SimpleArrayAdapter extends ArrayAdapter<String> {
	private List<String> Strings;
	private View v;

	public SimpleArrayAdapter(Context context, int resource, List<String> Strings) {
		super(context, resource, Strings);
		this.Strings = Strings;
	}

	@SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent){
		v = convertView;

		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.list_item_simple, null);
		}

		String item = Strings.get(position);

		if (item != null) {

			TextView name = (TextView) v.findViewById(R.id.name);
			name.setText(item);
		}
		return v;

	}
}
