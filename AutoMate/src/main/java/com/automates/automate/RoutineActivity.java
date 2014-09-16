package com.automates.automate;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.automates.automate.view.EditMultiChoiceModeListener;
import com.automates.automate.view.SimpleArrayAdapter;
import com.automates.automate.locations.UserLocation;
import com.automates.automate.locations.UserLocationService;
import com.automates.automate.pattern.Pattern;
import com.automates.automate.pattern.PatternService;
import com.automates.automate.pattern.StatusCode;
import com.automates.automate.routines.Routine;
import com.automates.automate.routines.RoutineService;
import com.automates.automate.settings.RingerProfiles;
import com.automates.automate.settings.Settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressLint("InflateParams")
public class RoutineActivity extends FragmentActivity {
    private EditText textName;

    private com.beardedhen.androidbootstrap.BootstrapButton addTrigger;
    private com.beardedhen.androidbootstrap.BootstrapButton addAction;
    private com.beardedhen.androidbootstrap.BootstrapButton saveButton;
    private com.beardedhen.androidbootstrap.BootstrapButton dismissButton;
    private PopupWindow popupWindow;
    
    private Routine routine;
    private List<String> triggers;
    private List<String> actions;
    private FragmentActivity activity;
    
    private CheckBox checkboxEnabled;

    private RelativeLayout loading;
    
    private int patternID;
    
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
    
    private boolean editing;
    
    
    //    private Button saveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);
        activity = this;
        textName = (EditText) findViewById(R.id.textName);
        addTrigger = (com.beardedhen.androidbootstrap.BootstrapButton) findViewById(R.id.addTrigger);
        addAction = (com.beardedhen.androidbootstrap.BootstrapButton) findViewById(R.id.addAction);
        saveButton = (com.beardedhen.androidbootstrap.BootstrapButton) findViewById(R.id.saveButton);
        triggersListView = (ListView) findViewById(R.id.triggersList);
        actionsListView = (ListView) findViewById(R.id.actionsList);
        loading = (RelativeLayout) findViewById(R.id.darken);
        checkboxEnabled = (CheckBox) findViewById(R.id.checkBoxEnabled);
        
        editing = false;
        
        routine = new Routine();
        actions = new ArrayList<String>();
        
        // Intent
        Intent intent = getIntent();
        final int routineID = intent.getIntExtra("routineID", -1);
        
        if (routineID != -1) {
        	for (Routine r: RoutineService.getInstance().getAllRoutines()) {
        		if (routineID == r.getId()) {
        			routine = r;
        			actions.add(r.actionsString());
        			textName.setText(r.getName());
        			if (r.getStatusCode() == StatusCode.IMPLEMENTED) {
        				checkboxEnabled.setChecked(true);
        			} else {
        				checkboxEnabled.setChecked(false);
        			}
        			editing = true;
        		}
        	}
        	
        }
        
        // Get pattern from intent if exists
        patternID = intent.getIntExtra("patternID", -1);
        
        // Dismiss Button
    	dismissButton = (com.beardedhen.androidbootstrap.BootstrapButton) findViewById(R.id.dismissButton);
    	
        if ((routine.getStatusCode() == StatusCode.AWAITING_APPROVAL)) {
        	dismissButton.setVisibility(View.VISIBLE);
        	checkboxEnabled.setChecked(true); // Tick 'enabled'
        	dismissButton.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View v) {
                    RoutineService.getInstance().remove(routine);
        			if (patternID != -1) {
	        			Pattern p = PatternService.getInstance().getPattern(patternID);
	                	p.setStatusCode(StatusCode.DECLINED);
                        PatternService.getInstance().updatePattern(p);
        			}
                    Intent resultIntent = new Intent();
                	setResult(Activity.RESULT_OK, resultIntent);
        			finish();
        		}
        	});
        } else {
        	LayoutParams params = dismissButton.getLayoutParams();
        	params.height = 0;
        	dismissButton.setLayoutParams(params);
        }
        
        
        // Add Trigger Listener
        addTrigger.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addClick(v, 0);
            }
        });
            
        // Add Action Listener
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
                

                
                if (checkboxEnabled.isChecked()) {
                	routine.setStatusCode(StatusCode.IMPLEMENTED);
                    if (patternID != -1) {
                    	Pattern p = PatternService.getInstance().getPattern(patternID);
                    	p.setStatusCode(StatusCode.IMPLEMENTED);
                        PatternService.getInstance().updatePattern(p);
                    }
                } else {
                	routine.setStatusCode(StatusCode.DECLINED);
                	if (patternID != -1) {
                    	Pattern p = PatternService.getInstance().getPattern(patternID);
                    	p.setStatusCode(StatusCode.IMPLEMENTED);
                        PatternService.getInstance().updatePattern(p);
                    }
                }
                if (editing) {
                	for (int i = 0; i < RoutineService.getInstance().getAllRoutines().size(); i++) {
                		Routine r = RoutineService.getInstance().getAllRoutines().get(i);
            			if (r.getId() == routineID) {
                            RoutineService.getInstance().set(i, r);
            				break;
                		}
                	}
                	
                } else {
                    RoutineService.getInstance().add(routine);
                }
                

                
                Intent resultIntent = new Intent();
            	setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
        
        // Trigger List Display
        triggers = routine.activeTriggerList();
        
//        triggersAdapter = new ArrayAdapter<String>(activity,
//                android.R.layout.simple_list_item_1, android.R.id.text1, triggers);
        triggersAdapter = new SimpleArrayAdapter(activity,
                R.layout.list_item_description, triggers);
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
//        actionsAdapter = new ArrayAdapter<String>(activity,
//                android.R.layout.simple_list_item_1, android.R.id.text1, actions);
        actionsAdapter = new SimpleArrayAdapter(activity,
                R.layout.list_item_simple, actions);
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
        	loading.setVisibility(View.INVISIBLE);
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
        loading.setVisibility(View.VISIBLE);
        hideSoftKeyboard();
        
        LayoutInflater layoutInflater  = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);  
        View popupView = layoutInflater.inflate(R.layout.popup_list, null);  
        popupWindow = new PopupWindow( popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);  
        
        List<String> options;
        if (type == 0) {
        	options = Arrays.asList(Settings.TIME, Settings.DAY, Settings.LOCATION, Settings.WIFI, Settings.MDATA);
            
        } else {
        	options = Arrays.asList(Settings.WIFI, Settings.MDATA, Settings.RINGER);
        }
        	
        final ListView listView = (ListView) popupView.findViewById(R.id.list);
        ArrayAdapter<String> adapter = new SimpleArrayAdapter(activity,
                R.layout.list_item_simple, options);
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
        
        if (s.equals(Settings.TIME)) {
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
                    loading.setVisibility(View.INVISIBLE);
                    popupWindow.dismiss();
                    
                }
            });
            
        } else if (s.equals(Settings.DAY)) {
            // Day
        	List<String> options = new ArrayList<String>();
        	options.addAll(Routine.daysMap.keySet());
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
                    loading.setVisibility(View.INVISIBLE);
                    popupWindow.dismiss();
                }
            });
            
            
        } else if (s.equals(Settings.LOCATION)) {
            // Location
            List<String> options = new ArrayList<String>();
            
            for (UserLocation ul: UserLocationService.getInstance().getAllUserLocations()) {
                options.add(ul.getName());
            }

            options.add("Unknown");

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
                    try {
                    String location = (String) b.getText();
                    if (location == "Unknown") {
                        routine.setLocation(""+-1);
                    } else {
                        for (UserLocation ul : UserLocationService.getInstance().getAllUserLocations()) {
                            if (ul.getName().equals(location)) {
                                routine.setLocation("" + ul.getId());
                                break;
                            }
                        }
                    }
                    } catch (NullPointerException e) {
                    	
                    }
                    triggers.clear();
                    triggers.addAll(routine.activeTriggerList());
                    triggersAdapter.notifyDataSetChanged();
                    loading.setVisibility(View.INVISIBLE);
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
                    if (string.equals(Settings.WIFI)) {
                        routine.setWifi(onoff);
                    } else if (string.equals(Settings.MDATA)) {
                        routine.setmData(onoff);
                    }
                    
                    triggers.clear();
                    triggers.addAll(routine.activeTriggerList());
                    triggersAdapter.notifyDataSetChanged();
                    loading.setVisibility(View.INVISIBLE);
                    popupWindow.dismiss();
                }
            });
        }
        
        
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
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
        
        if (s.equals(Settings.RINGER)) {
        	List<String> options = new ArrayList<String>();
        	options.addAll(RingerProfiles.ringerMap.keySet());
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
                    int ringer = RingerProfiles.ringerToInt((String) b.getText());
                    routine.setEventCategory(Settings.RINGER);
                    routine.setEvent("" + ringer);
                    
                    actions.clear();
                    actions.add(routine.actionsString());
                    actionsAdapter.notifyDataSetChanged();
                    loading.setVisibility(View.INVISIBLE);
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
                    actions.add(routine.actionsString());
                    actionsAdapter.notifyDataSetChanged();
                    loading.setVisibility(View.INVISIBLE);
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
							if (trigger.equals(Settings.TIME)) {
								routine.setHour(-1);
								routine.setMinute(-1);
							} else if (trigger.equals(Settings.DAY)) {
								routine.setDay("");
							} else if (trigger.equals(Settings.LOCATION)) {
								routine.setLocation("");
							} else if (trigger.equals(Settings.WIFI)) {
								routine.setWifi("");
							} else if (trigger.equals(Settings.MDATA)) {
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
