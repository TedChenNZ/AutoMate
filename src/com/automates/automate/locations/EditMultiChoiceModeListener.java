package com.automates.automate.locations;

import java.util.Collections;
import java.util.List;

import com.automates.automate.LocationActivity;
import com.automates.automate.PhoneState;
import com.automates.automate.R;

import android.app.Activity;
import android.content.Intent;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AbsListView.MultiChoiceModeListener;

public class EditMultiChoiceModeListener implements MultiChoiceModeListener {

	private int fragmentID;
	private List<Integer> selected;
	private ArrayAdapter<?> adapter;
	private Activity activity;
	
	
	public EditMultiChoiceModeListener(int fragmentID, List<Integer> selected, ArrayAdapter<?> adapter) {
		this.fragmentID = fragmentID;
		this.selected = selected;
		this.adapter = adapter;
	}
	
	public EditMultiChoiceModeListener(Activity activity, int fragmentID, List<Integer> selected, ArrayAdapter<?> adapter) {
		this.activity = activity;
		this.fragmentID = fragmentID;
		this.selected = selected;
		this.adapter = adapter;
	}
	
	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		switch (fragmentID) {
			case 1: // Locations
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
		default:
				return false;
		}
		
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		MenuInflater inflater = mode.getMenuInflater();
		inflater.inflate(R.menu.edit, menu);
		return true;
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
		selected.clear();
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		return false;
	}

	@Override
	public void onItemCheckedStateChanged(ActionMode mode, int position,
			long id, boolean checked) {
		if (checked) {
			selected.add(position);
		} else {
			selected.remove((Object)position);
		}
		
		mode.setTitle(Integer.toString(selected.size()) + " Selected");
		if (selected.size() > 1) {
			mode.getMenu().findItem(R.id.action_edit).setVisible(false);
		} else {
			mode.getMenu().findItem(R.id.action_edit).setVisible(true);
		}
		
	}

}
