package com.automates.automate.view.activities;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ActionMode;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.automates.automate.R;
import com.automates.automate.model.UserLocation;
import com.automates.automate.services.PhoneService;
import com.automates.automate.services.locations.UserLocationService;
import com.automates.automate.model.Pattern;
import com.automates.automate.services.pattern.PatternService;
import com.automates.automate.services.pattern.StatusCode;
import com.automates.automate.model.Routine;
import com.automates.automate.services.routines.RoutineService;
import com.automates.automate.services.settings.RingerProfiles;
import com.automates.automate.services.settings.Settings;
import com.automates.automate.view.lists.EditActionModeCallback;
import com.automates.automate.view.lists.EditMultiChoiceModeListener;
import com.automates.automate.view.lists.SimpleArrayAdapter;

import java.util.ArrayList;
import java.util.Calendar;
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
        final Context context = this;
        if ((routine.getStatusCode() == StatusCode.AWAITING_APPROVAL) || routine.getStatusCode() == StatusCode.IN_DEV) {
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
                    PhoneService.getInstance().update(context);
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
                addTrigger(v);
            }
        });


        // Add Action Listener
        addAction.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actions.size() == 0) {
                    addAction(v);
                } else {
                    Toast.makeText(getBaseContext(), "Only 1 Action allowed", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // Save Button Listener
        saveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate Inputs
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


                // Finish Activity with ok result
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

    private void addTrigger(View v) {
        final CharSequence[] items = {
                Settings.TIME, Settings.DAY, Settings.LOCATION, Settings.WIFI, Settings.MDATA
        };

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Add Trigger");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                addTriggerOptions((String)items[item]);
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void addAction(View v) {
        final CharSequence[] items = {
                Settings.WIFI, Settings.MDATA, Settings.RINGER
        };

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Add Action");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                addActionOptions((String)items[item]);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }



    private void addTriggerOptions(final String item) {
        if (item.equals(Settings.TIME)) {
            // Time
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    routine.setHour(timePicker.getCurrentHour());
                    routine.setMinute(timePicker.getCurrentMinute());

                    triggers.clear();
                    triggers.addAll(routine.activeTriggerList());
                    triggersAdapter.notifyDataSetChanged();
                }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();


        } else if (item.equals(Settings.DAY)) {
            // Day
            final CharSequence[] options = Routine.daysMap.keySet()
                    .toArray(new CharSequence[Routine.daysMap.keySet().size()]);

            final AlertDialog.Builder innerBuilder = new AlertDialog.Builder(activity);
            innerBuilder.setTitle("If Day is");
            innerBuilder.setItems(options, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    int day = Routine.dayToInt((String) options[item]);
                    routine.setDay("" + day);
                    triggers.clear();
                    triggers.addAll(routine.activeTriggerList());
                    triggersAdapter.notifyDataSetChanged();
                }
            });
            AlertDialog innerAlert = innerBuilder.create();
            innerAlert.show();
        } else if (item.equals(Settings.LOCATION)) {
            // Location
            List<String> locations = new ArrayList<String>();
            for (UserLocation ul: UserLocationService.getInstance().getAllUserLocations()) {
                locations.add(ul.getName());
            }
            locations.add("Unknown");
            final CharSequence[] options = locations.toArray(
                    new CharSequence[locations.size()]
            );
            final AlertDialog.Builder innerBuilder = new AlertDialog.Builder(activity);
            innerBuilder.setTitle("If Location is");
            innerBuilder.setItems(options, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    try {
                        String location = (String)options[item];
                        if (location.equals("Unknown")) {
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
                }
            });
            AlertDialog innerAlert = innerBuilder.create();
            innerAlert.show();


        } else if (item.equals(Settings.WIFI) || item.equals(Settings.MDATA)) {
            // Wifi and MData
            final CharSequence[] options = {
                    "On", "Off"
            };

            final AlertDialog.Builder innerBuilder = new AlertDialog.Builder(activity);
            if (item.equals(Settings.WIFI)) {
                innerBuilder.setTitle("If Wifi is");
            } else {
                innerBuilder.setTitle("If Mobile Data is");
            }
            innerBuilder.setItems(options, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int innerItem) {
                    String sText = (String) options[innerItem];
                    String onoff = "false";
                    if (sText.equals("On")) {
                        onoff = "true";
                    }
                    if (item.equals(Settings.WIFI)) {
                        routine.setWifi(onoff);
                    } else {
                        routine.setmData(onoff);
                    }

                    triggers.clear();
                    triggers.addAll(routine.activeTriggerList());
                    triggersAdapter.notifyDataSetChanged();
                }
            });
            AlertDialog innerAlert = innerBuilder.create();
            innerAlert.show();
        }
    }

    private void addActionOptions(String item) {
        if (item.equals(Settings.WIFI) || item.equals(Settings.MDATA)) {
            // Wifi
            final CharSequence[] options = {
                    "On", "Off"
            };

            final AlertDialog.Builder innerBuilder = new AlertDialog.Builder(activity);
            if (item.equals(Settings.WIFI)) {
                innerBuilder.setTitle("Set Wifi to");
            } else {
                innerBuilder.setTitle("Set Mobile Data to");
            }

            innerBuilder.setItems(options, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    String sText = (String) options[item];
                    String onoff = "false";
                    if (sText.equals("On")) {
                        onoff = "true";
                    }
                    routine.setEventCategory(Settings.WIFI);
                    routine.setEvent(onoff);

                    actions.clear();
                    actions.add(routine.actionsString());
                    actionsAdapter.notifyDataSetChanged();
                }
            });
            AlertDialog innerAlert = innerBuilder.create();
            innerAlert.show();
        } else if (item.equals(Settings.RINGER)) {
            // Ringer
            final CharSequence[] options = RingerProfiles.ringerMap.keySet()
                    .toArray(new CharSequence[RingerProfiles.ringerMap.keySet().size()]);

            final AlertDialog.Builder innerBuilder = new AlertDialog.Builder(activity);
            innerBuilder.setTitle("Set Ringer to");
            innerBuilder.setItems(options, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    int ringer = RingerProfiles.ringerToInt((String)options[item]);
                    routine.setEventCategory(Settings.RINGER);
                    routine.setEvent("" + ringer);

                    actions.clear();
                    actions.add(routine.actionsString());
                    actionsAdapter.notifyDataSetChanged();
                }
            });
            AlertDialog innerAlert = innerBuilder.create();
            innerAlert.show();

        }
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
//                      Intent intent = new Intent(activity, RoutineActivity.class);
//                      intent.putExtra("EditItem", selected.get(0));
//                      activity.startActivityForResult(intent, 0);

                        addTriggerOptions(trigger);
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

                            addActionOptions(action);
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
