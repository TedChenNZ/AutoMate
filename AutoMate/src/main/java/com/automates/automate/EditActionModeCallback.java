package com.automates.automate;

import java.util.List;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;


public class EditActionModeCallback implements ActionMode.Callback {
	
	private ListView lv;
	private List<Integer> selected;
	
	public EditActionModeCallback(ListView lv) {
		this.lv = lv;
	}
	
	public EditActionModeCallback(ListView lv, List<Integer>  selected) {
		this.lv = lv;
		this.selected = selected;
	}
	
	// Called when the user selects a contextual menu item
	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//		switch (item.getItemId()) {
//			case R.id.action_edit:
//				return true;
//			case R.id.action_discard:
//				return true;
//			default:
//				return false;
//		}
		
		return false;
	}
	
    // Called when the action mode is created; startActionMode() was called
	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		MenuInflater inflater = mode.getMenuInflater();
		inflater.inflate(R.menu.edit, menu);
		return true;
	}
	
	@Override
	public void onDestroyActionMode(ActionMode mode) {
		//Remove All
		if (lv != null) {
			for (int i = 0; i < lv.getCount(); i++) {
				lv.setItemChecked(i, false);
			}
		}
		selected.clear();
		
	}
	
	// Called each time the action mode is shown. Always called after onCreateActionMode, but
    // may be called multiple times if the mode is invalidated.
	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		return false; // Return false if nothing is done
	}
	
}
