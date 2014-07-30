package com.automates.automate;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.automates.automate.locations.EditMultiChoiceModeListener;
import com.automates.automate.locations.UserLocation;
import com.automates.automate.pattern.StatusCode;
import com.automates.automate.routines.Routine;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;

@SuppressLint("InflateParams")
public class RoutineActivity extends FragmentActivity {
	private EditText textName;

	private Button addTrigger;
	private Button addAction;
	private Button saveButton;
	private PopupWindow popupWindow;

	private Routine routine;
	private List<String> triggers;
	private List<String> actions;
	private FragmentActivity activity;

	// trigger list
	private ListView triggersListView;
	private ArrayAdapter<String> triggersAdapter;
	private List<Integer> triggersSelected = new ArrayList<Integer>();
	private ActionMode.Callback triggersActionModeCallback;
	private ActionMode triggersActionMode;
	private EditMultiChoiceModeListener triggersModeListener;

	// action list
	private ListView actionsListView;
	private ArrayAdapter<String> actionsAdapter;
	private List<Integer> actionsSelected = new ArrayList<Integer>();
	private ActionMode.Callback actionsActionModeCallback;
	private ActionMode actionsActionMode;
	private EditMultiChoiceModeListener actionsModeListener;


	//    private Button saveButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_routine_activity);
		activity = this;
		textName = (EditText) findViewById(R.id.textName);
		addTrigger = (Button) findViewById(R.id.addTrigger);
		addAction = (Button) findViewById(R.id.addAction);
		saveButton = (Button) findViewById(R.id.saveButton);
		triggersListView = (ListView) findViewById(R.id.triggersList);
		actionsListView = (ListView) findViewById(R.id.actionsList);


		routine = new Routine();
		actions = new ArrayList<String>();

		addTrigger.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addClick(v, 0);
			}
		});


		addAction.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (actions.size() == 0) {
					addClick(v, 1);
				} else {
					Toast.makeText(getBaseContext(), "Only 1 Action allowed", Toast.LENGTH_SHORT).show();
				}

			}
		});

		saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = (String) textName.getText().toString();
				if (name == null || name.length() == 0) {
					Toast.makeText(getBaseContext(), "No Name was set", Toast.LENGTH_SHORT).show();
					return;
				}
				if (triggers.size() == 0) {
					Toast.makeText(getBaseContext(), "Must have at least 1 Trigger", Toast.LENGTH_SHORT).show();
					return;
				}
				if (actions.size() == 0) {
					Toast.makeText(getBaseContext(), "Must have an Action", Toast.LENGTH_SHORT).show();
					return;
				}
				routine.setName(name);
				routine.setStatusCode(StatusCode.IMPLEMENTED);
				PhoneState.getRoutineDb().addRoutine(routine);

				Intent resultIntent = new Intent();
				setResult(Activity.RESULT_OK, resultIntent);
				finish();
			}
		});

		// Trigger List Display
		triggers = routine.activeTriggerList();

		triggersAdapter = new ArrayAdapter<String>(activity,
				android.R.layout.simple_list_item_1, android.R.id.text1, triggers);
		triggersListView.setAdapter(triggersAdapter);
		triggersListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

		// List View Mode Listener and CAB

		triggersModeListener = new TriggersModeListener(activity, triggersSelected, triggersAdapter);
		triggersListView.setMultiChoiceModeListener(triggersModeListener);

		triggersActionModeCallback = new EditActionModeCallback(triggersListView, triggersSelected);


		triggersListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				if (triggersSelected.contains(position)) {
					triggersListView.setItemChecked(position, false);
					if (triggersActionMode != null && triggersSelected.size() == 0) {
						triggersActionMode.finish();
					}
				} else {
					// For multiple selections
					if (triggersActionMode == null || triggersSelected.size() == 0) {
						triggersActionMode = activity.startActionMode(triggersActionModeCallback);
					}
					triggersListView.setItemChecked(position, true);

				}

			}
		});


		// Action List Display
		// Adapter
		actionsAdapter = new ArrayAdapter<String>(activity,
				android.R.layout.simple_list_item_1, android.R.id.text1, actions);
		actionsListView.setAdapter(actionsAdapter);
		// List View Initialize
		actionsListView.setAdapter(actionsAdapter);
		actionsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

		// List View Mode Listener and CAB
		actionsModeListener = new ActionsModeListener(this, actionsSelected, actionsAdapter);
		actionsListView.setMultiChoiceModeListener(actionsModeListener);
		actionsActionModeCallback = new EditActionModeCallback(actionsListView, actionsSelected);
		actionsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				if (actionsSelected.contains(position)) {
					actionsListView.setItemChecked(position, false);
					if (actionsActionMode != null && actionsSelected.size() == 0) {
						actionsActionMode.finish();
					}
				} else {
					// For multiple selections
					if (actionsActionMode == null || actionsSelected.size() == 0) {
						actionsActionMode = startActionMode(actionsActionModeCallback);
					}
					actionsListView.setItemChecked(position, true);

				}

			}
		});



	}

	/**
	 * Hides the soft keyboard
	 */
	private void hideSoftKeyboard() {
		if(getCurrentFocus()!=null) {
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		}
	}

	@Override
	public void onBackPressed() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
		} else {
			super.onBackPressed();
		}
	}

	private void addClick(View v, final int type) {
		// types: 0 = trigger, 1 = action

				if (popupWindow != null && popupWindow.isShowing()) {
					return;
				}
				hideSoftKeyboard();

				LayoutInflater layoutInflater  = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);  
				View popupView = layoutInflater.inflate(R.layout.popup_list, null);  
				popupWindow = new PopupWindow( popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);  

				List<String> options;
				if (type == 0) {
					options = Arrays.asList(Routine.TIME, Routine.DAY, Routine.LOCATION, Routine.WIFI, Routine.MDATA);

				} else {
					options = Arrays.asList(Routine.WIFI, Routine.MDATA, Routine.RINGER);
				}

				final ListView listView = (ListView) popupView.findViewById(R.id.list);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
						android.R.layout.simple_list_item_1, android.R.id.text1, options);
				listView.setAdapter(adapter);
				// ListView Item Click Listener
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						// ListView Clicked item value
						String itemValue = (String) listView.getItemAtPosition(position);

						// Show Alert 
						popupWindow.dismiss();
						if (type == 0)  {
							popupTriggerOptions(view, itemValue);
						} else {
							popupActionOptions(view, itemValue);
						}
					}

				}); 

				popupWindow.setOutsideTouchable(true);
				popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
	}

	private void popupTriggerOptions(View v, String s) {
		if (popupWindow != null && popupWindow.isShowing()) {
			return;
		}
		hideSoftKeyboard();

		LayoutInflater layoutInflater  = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);  
		final View popupView = layoutInflater.inflate(R.layout.popup_options, null);  
		popupWindow = new PopupWindow( popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);  

		TextView desc = (TextView) popupView.findViewById(R.id.textDescription);
		desc.setText("When '" + s + "' is:");
		Button buttonOK = (Button) popupView.findViewById(R.id.buttonOK);

		final RadioGroup radioGroup = (RadioGroup) popupView.findViewById(R.id.radioGroup);

		LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
				RadioGroup.LayoutParams.WRAP_CONTENT,
				RadioGroup.LayoutParams.WRAP_CONTENT);

		RadioButton rb;

		if (s.equals(Routine.TIME)) {
			// Time
			final TimePicker timePicker = (TimePicker) popupView.findViewById(R.id.timePicker);
			timePicker.setVisibility(View.VISIBLE);
			buttonOK.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					routine.setHour(timePicker.getCurrentHour());
					routine.setMinute(timePicker.getCurrentMinute());

					triggers.clear();
					triggers.addAll(routine.activeTriggerList());
					triggersAdapter.notifyDataSetChanged();
					popupWindow.dismiss();
				}
			});

		} else if (s.equals(Routine.DAY)) {
			// Day
			List<String> options = Arrays.asList("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
			Collections.reverse(options);
			int i = 0;
			for (String l: options) {
				rb = new RadioButton(activity);
				rb.setText(l);
				rb.setId(i);
				radioGroup.addView(rb, 0, layoutParams);
				i++;
			}
			radioGroup.check(i-1);
			buttonOK.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// Gets a reference to "selected" radio button
					int selected = radioGroup.getCheckedRadioButtonId();
					RadioButton b = (RadioButton) popupView.findViewById(selected);
					int day = Routine.dayToInt((String) b.getText());
					routine.setDay("" + day);
					triggers.clear();
					triggers.addAll(routine.activeTriggerList());
					triggersAdapter.notifyDataSetChanged();
					popupWindow.dismiss();
				}
			});


		} else if (s.equals(Routine.LOCATION)) {
			// Location
			List<String> options = new ArrayList<String>();

			for (UserLocation ul: PhoneState.getLocationsList()) {
				options.add(ul.getName());
			}

			Collections.reverse(options);
			int i = 0;
			for (String l: options) {
				rb = new RadioButton(activity);
				rb.setText(l);
				rb.setId(i);
				radioGroup.addView(rb, 0, layoutParams);
				i++;
			}
			radioGroup.check(i-1);

			buttonOK.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// Gets a reference to "selected" radio button
					int selected = radioGroup.getCheckedRadioButtonId();
					RadioButton b = (RadioButton) popupView.findViewById(selected);
					String location = (String) b.getText();
					for (UserLocation ul: PhoneState.getLocationsList()) {
						if (ul.getName().equals(location)) {
							routine.setLocation("" + ul.getId());
							break;
						}
					}
					triggers.clear();
					triggers.addAll(routine.activeTriggerList());
					triggersAdapter.notifyDataSetChanged();
					popupWindow.dismiss();

				}
			});
		} else {
			// On/Off
			final String string = s;
			List<String> options = Arrays.asList("On", "Off");
			Collections.reverse(options);
			int i = 0;
			for (String l: options) {
				rb = new RadioButton(activity);
				rb.setText(l);
				rb.setId(i);
				radioGroup.addView(rb, 0, layoutParams);
				i++;
			}
			radioGroup.check(i-1);
			buttonOK.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// Gets a reference to "selected" radio button
					int selected = radioGroup.getCheckedRadioButtonId();
					RadioButton b = (RadioButton) popupView.findViewById(selected);
					String sText = (String) b.getText();
					String onoff = "false";
					if (sText.equals("On")) {
						onoff = "true";
					}
					if (string.equals(Routine.WIFI)) {
						routine.setWifi(onoff);
					} else if (string.equals(Routine.MDATA)) {
						routine.setmData(onoff);
					}

					triggers.clear();
					triggers.addAll(routine.activeTriggerList());
					triggersAdapter.notifyDataSetChanged();
					popupWindow.dismiss();
				}
			});
		}


		popupWindow.setOutsideTouchable(true);
		popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
	}

	@SuppressWarnings("deprecation")
	public void notification(String subject, String body, int id){
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);

		int NOT_ID = 1;
		int icon = R.drawable.ic_routines;
		CharSequence tickerText = "Automate - Pattern recognised!";
		long when = System.currentTimeMillis();

		int requestID = (int) System.currentTimeMillis();
		Notification notification = new Notification(icon, tickerText, when);
		Context context = getApplicationContext();
		Intent notificationIntent = new Intent(this, RoutineActivity.class);
		notificationIntent.putExtra("routineID", id);
		PendingIntent contentIntent = PendingIntent.getActivity(this, requestID, notificationIntent, 0);
		notificationIntent.setData((Uri.parse("mystring"+requestID)));
		notification.setLatestEventInfo(context, subject, body, contentIntent);
		notification.flags += Notification.FLAG_ONGOING_EVENT;
		notification.flags += Notification.FLAG_AUTO_CANCEL;
		mNotificationManager.notify(NOT_ID, notification);

		//	String ns = Context.NOTIFICATION_SERVICE;
		//	NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		//	
		//	int NOT_ID = 1;
		//	
		//	int requestID = (int) System.currentTimeMillis();
		//	Notificationt.Builder notification = new Notification.Builder(this)
		//        .setContentTitle(subject)
		//        .setContentText(body)
		//        .setTicker("Automate - Pattern recognised!")
		//        .setSmallIcon(R.drawable.ic_routines)
		//        .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
		//                R.drawable.ic_routines))
		//        .build();
		//	Context context = getApplicationContext();
		//	Intent notificationIntent = new Intent(this, RoutineActivity.class);
		//	notificationIntent.putExtra("routineID", id);
		//	PendingIntent contentIntent = PendingIntent.getActivity(this, requestID, notificationIntent, 0);
		//	//notification.setLatestEventInfo(context, subject, body, contentIntent);
		//	notification.setContentIntent(contentIntent);
		//	notification.flags += Notification.FLAG_ONGOING_EVENT;
		//	notification.flags += Notification.FLAG_AUTO_CANCEL;
		//	mNotificationManager.notify(NOT_ID, notification);
	}

	private void popupActionOptions(View v, String s) {
		if (popupWindow != null && popupWindow.isShowing()) {
			return;
		}
		hideSoftKeyboard();

		LayoutInflater layoutInflater  = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);  
		final View popupView = layoutInflater.inflate(R.layout.popup_options, null);  
		popupWindow = new PopupWindow( popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);  

		TextView desc = (TextView) popupView.findViewById(R.id.textDescription);
		desc.setText("Set '" + s + "' to:");
		Button buttonOK = (Button) popupView.findViewById(R.id.buttonOK);

		final RadioGroup radioGroup = (RadioGroup) popupView.findViewById(R.id.radioGroup);

		LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
				RadioGroup.LayoutParams.WRAP_CONTENT,
				RadioGroup.LayoutParams.WRAP_CONTENT);

		RadioButton rb;

		if (s.equals(Routine.RINGER)) {
			List<String> options = Arrays.asList("Silent and No Vibrate", "Silent and Vibrate", "Normal and No Vibrate", "Normal and Vibrate");
			Collections.reverse(options);
			int i = 0;
			for (String l: options) {
				rb = new RadioButton(activity);
				rb.setText(l);
				rb.setId(i);
				radioGroup.addView(rb, 0, layoutParams);
				i++;
			}
			radioGroup.check(i-1);
			buttonOK.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// Gets a reference to "selected" radio button
					int selected = radioGroup.getCheckedRadioButtonId();
					RadioButton b = (RadioButton) popupView.findViewById(selected);
					int ringer = Routine.ringerToInt((String) b.getText());
					routine.setEventCategory(Routine.RINGER);
					routine.setEvent("" + ringer);

					actions.clear();
					actions.add(Routine.RINGER + ": " + ringer);
					actionsAdapter.notifyDataSetChanged();
					popupWindow.dismiss();
				}
			});
		} else {
			// On/Off
			// Wifi and mData
			final String string = s;
			List<String> options = Arrays.asList("On", "Off");
			Collections.reverse(options);
			int i = 0;
			for (String l: options) {
				rb = new RadioButton(activity);
				rb.setText(l);
				rb.setId(i);
				radioGroup.addView(rb, 0, layoutParams);
				i++;
			}
			radioGroup.check(i-1);
			buttonOK.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// Gets a reference to "selected" radio button
					int selected = radioGroup.getCheckedRadioButtonId();
					RadioButton b = (RadioButton) popupView.findViewById(selected);
					String sText = (String) b.getText();
					String onoff = "false";
					if (sText.equals("On")) {
						onoff = "true";
					}
					routine.setEventCategory(string);
					routine.setEvent(onoff);


					actions.clear();
					actions.add(string+ ": " + onoff);
					actionsAdapter.notifyDataSetChanged();
					popupWindow.dismiss();
				}
			});
		}


		popupWindow.setOutsideTouchable(true);
		popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
	}




	private class TriggersModeListener extends EditMultiChoiceModeListener {

		private List<Integer> selected;
		private ArrayAdapter<String> adapter;
		private Activity activity;

		public TriggersModeListener(Activity activity,
				List<Integer> selected, ArrayAdapter<String> adapter) {
			super(selected);
			this.activity = activity;
			this.selected = selected;
			this.adapter = adapter;
		}


		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			String s = "";
			String trigger = "";
			if (selected != null) {
				Collections.sort(selected);
				Collections.reverse(selected);


			}

			switch (item.getItemId()) {
			case R.id.action_edit:
				if (selected != null && selected.size() == 1) {
					s = (String) adapter.getItem(selected.get(0));
					trigger = s.split(":")[0];
					//						Intent intent = new Intent(activity, RoutineActivity.class);
					//						intent.putExtra("EditItem", selected.get(0));
					//		            	activity.startActivityForResult(intent, 0);

					popupTriggerOptions(activity.findViewById(android.R.id.content), trigger);
				}
				adapter.notifyDataSetChanged();
				mode.finish();
				return true;
			case R.id.action_discard:

				if (selected != null) {
					for (int i = 0; i < selected.size(); i++) {
						s = (String) adapter.getItem(selected.get(i));
						trigger = s.split(":")[0];
						if (trigger.equals(Routine.TIME)) {
							routine.setHour(-1);
							routine.setMinute(-1);
						} else if (trigger.equals(Routine.DAY)) {
							routine.setDay("");
						} else if (trigger.equals(Routine.LOCATION)) {
							routine.setLocation("");
						} else if (trigger.equals(Routine.WIFI)) {
							routine.setWifi("");
						} else if (trigger.equals(Routine.MDATA)) {
							routine.setmData("");
						}

						adapter.remove(s);
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

	private class ActionsModeListener extends EditMultiChoiceModeListener {

		private List<Integer> selected;
		private ArrayAdapter<String> adapter;
		private Activity activity;

		public ActionsModeListener(Activity activity,
				List<Integer> selected, ArrayAdapter<String> adapter) {
			super(selected);
			this.activity = activity;
			this.selected = selected;
			this.adapter = adapter;
		}


		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			String s = "";
			String action = "";
			if (selected != null) {
				Collections.sort(selected);
				Collections.reverse(selected);
			}

			switch (item.getItemId()) {
			case R.id.action_edit:
				if (selected != null && selected.size() == 1) {
					if (selected != null && selected.size() == 1) {
						s = (String) adapter.getItem(selected.get(0));
						action = s.split(":")[0];

						popupActionOptions(activity.findViewById(android.R.id.content), action);
					}
				}
				adapter.notifyDataSetChanged();
				mode.finish();
				return true;
			case R.id.action_discard:

				if (selected != null) {
					for (int i = 0; i < selected.size(); i++) {
						s = (String) adapter.getItem(selected.get(i));
						action = s.split(":")[0];

						routine.setEvent("");
						routine.setEventCategory("");
						adapter.remove(s);
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
