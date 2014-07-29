package com.automates.automate;

import com.automates.automate.routines.Routine;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RoutineActivity extends FragmentActivity {

    //    private Button saveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_location_activity);
	Routine r = new Routine(10, "tr1", "false", "Wifi", 22, 17, "", "", "", "", 1);
	PhoneState.getRoutineDb().addRoutine(r);

    }

    //        // Setting click event listener for the add button
    //        saveButton = (Button) findViewById(R.id.saveButton);
    //        saveButton.setOnClickListener(new OnClickListener() {
    //        	
    //            @Override
    //            public void onClick(View v) {
    //            	Log.d("test", "button clicked");
    //            }
    //		});
    //	}
}
