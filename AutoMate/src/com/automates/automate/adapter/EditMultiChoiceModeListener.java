package com.automates.automate.adapter;

import java.util.List;

import com.automates.automate.R;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView.MultiChoiceModeListener;

public class EditMultiChoiceModeListener implements MultiChoiceModeListener {

	private List<Integer> selected;
	
	
//	public EditMultiChoiceModeListener(int fragmentID, List<Integer> selected, ArrayAdapter<?> adapter) {
//		this.fragmentID = fragmentID;
//		this.selected = selected;
//		this.adapter = adapter;
//	}
	
	public EditMultiChoiceModeListener(List<Integer> selected) {
		this.selected = selected;
	}
	
	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		return false;
		
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
