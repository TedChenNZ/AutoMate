package com.automates.automate;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.automates.automate.locations.UserLocation;
import com.automates.automate.routines.Routine;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
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

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;

@SuppressLint("InflateParams")
public class RoutineActivity extends FragmentActivity {
    private EditText textName;
    private ListView triggersList;
    private ListView actionsList;
    private Button addTrigger;
    private Button addAction;
    private Button saveButton;
    private PopupWindow popupWindow;
    
    private Routine routine;
    private List<String> triggers;
    private List<String> actions;
    private List<String> allTriggers;
    private FragmentActivity activity;
    
    
    //    private Button saveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_activity);
    //  Routine r = new Routine(10, "rName1", "WiFi", "false", 0, 52, "1", "Home", "false", "false", 1);
    //  PhoneState.getRoutineDb().addRoutine(r);
        activity = this;
        textName = (EditText) findViewById(R.id.textName);
        addTrigger = (Button) findViewById(R.id.addTrigger);
        addAction = (Button) findViewById(R.id.addAction);
        saveButton = (Button) findViewById(R.id.saveButton);
        
        allTriggers = Arrays.asList("Time", "Day", "Location", "Wifi", "Mobile Data");
        
        routine = new Routine();
        triggers = new ArrayList<String>();
        actions = new ArrayList<String>();
    
        addTrigger.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addTriggerClick(v);
            }
        });
            
        
        addAction.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
        
        saveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textName.getText() == null || textName.getText().equals("")) {
                    Toast.makeText(getBaseContext(), "No Name was set", Toast.LENGTH_SHORT).show();
                    return;
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
    
    private void addTriggerClick(View v) {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        hideSoftKeyboard();
        
        LayoutInflater layoutInflater  = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);  
        View popupView = layoutInflater.inflate(R.layout.popup_list, null);  
        popupWindow = new PopupWindow( popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);  
        
        List<String> aTriggers = allTriggers;
        
        final ListView listView = (ListView) popupView.findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                android.R.layout.simple_list_item_1, android.R.id.text1, aTriggers);
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
                    
                  popupOptions(view, itemValue);
              }

        }); 
        
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
    }
    
    private void popupOptions(View v, String s) {
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
        
        if (s.equals("Time")) {
            // Time
            final TimePicker timePicker = (TimePicker) popupView.findViewById(R.id.timePicker);
            timePicker.setVisibility(View.VISIBLE);
            buttonOK.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    routine.setHour(timePicker.getCurrentHour());
                    routine.setMinute(timePicker.getCurrentMinute());
                    popupWindow.dismiss();
                }
            });
            
        } else if (s.equals("Day")) {
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
            
            buttonOK.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Gets a reference to "selected" radio button
                    int selected = radioGroup.getCheckedRadioButtonId();
                    RadioButton b = (RadioButton) popupView.findViewById(selected);
                    int day = dayToInt((String) b.getText());
                    routine.setDay("" + day);
                    popupWindow.dismiss();
                }
            });
            
            
        } else if (s.equals("Location")) {
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
                    if (string.equals("Wifi")) {
                        routine.setWifi(onoff);
                    } else if (string.equals("Mobile Data")) {
                        routine.setmData(onoff);
                    }
                    
                    popupWindow.dismiss();
                }
            });
        }
        
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
    }
    
    
    private int dayToInt(String day) {
         Map<String,Integer> mp = new HashMap<String,Integer>();
         
         mp.put("Sunday",0);
         mp.put("Monday",1);
         mp.put("Tuesday",2);
         mp.put("Wednesday",3);
         mp.put("Thrusday",4);
         mp.put("Friday",5);
         mp.put("Saturday",6);
         
         return mp.get(day).intValue();
    }
}
