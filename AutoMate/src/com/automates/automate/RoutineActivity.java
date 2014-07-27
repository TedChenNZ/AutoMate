package com.automates.automate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RoutineActivity extends FragmentActivity {
	
	private Button saveButton;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_activity);
        
        // Setting click event listener for the add button
        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new OnClickListener() {
        	
            @Override
            public void onClick(View v) {
            	// Do things
            }
		});
	}
}
