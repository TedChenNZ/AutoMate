package com.automates.automate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FirstRunActivity extends Activity {
	private TextView welcome;
	private Animation animFadein;
	private Animation animFadeout;
	private RelativeLayout layout;
	private int screen;
	private Button yes;
	private Button no;
	private Activity activity;
	private TextView continues;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        
        setContentView(R.layout.activity_first_run);
        activity = this;
        continues = (TextView) findViewById(R.id.continues);
        welcome = (TextView) findViewById(R.id.welcome);
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        animFadeout = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_out);
        
        screen = 0;
        
        yes = (Button) findViewById(R.id.yes);
        no = (Button) findViewById(R.id.no);
        
        
        // set listener for screen
        layout = (RelativeLayout) findViewById(R.id.layout);
        layout.setSoundEffectsEnabled(false);
        layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
            	if (screen < 3 )
            		updateScreen();
            }

         });
        
        yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, LocationActivity.class);
				String location = "";
				
				switch(screen) {
					case 3:
						location = "Work";
						intent.putExtra("FIRSTRUN", location);   
		            	startActivityForResult(intent, 0);
						break;
					case 4:
						location = "School";
						intent.putExtra("FIRSTRUN", location);   
		            	startActivityForResult(intent, 0);
						break;
					default:
						
						break;
				}
				
			}
        	
        });
        
        no.setOnClickListener(new OnClickListener() {
        	
        	@Override
        	public void onClick(View v) {
        		screen++;
        		updateScreen();
        	}
        });
        
        welcome.startAnimation(animFadein);
        
	}
	
	private void updateScreen() {
		Log.d("FirstRun", ""+screen);
		switch (screen) {
			case 0:
				welcome.startAnimation(animFadeout);
				welcome.setText("To help AutoMate your life, please answer a few questions to make your day more efficient");
				welcome.startAnimation(animFadein);
				screen++;
				break;
			case 1:
				welcome.startAnimation(animFadeout);
				welcome.setText("First, set your home location");
				welcome.startAnimation(animFadein);
				screen++;
				break;
			case 2:
				Intent intent = new Intent(activity, LocationActivity.class);
				intent.putExtra("FIRSTRUN", "Home");   
            	startActivityForResult(intent, 0);
				break;
			case 3:
				continues.setVisibility(4);
				welcome.setText("Do you work away from home?");
				welcome.startAnimation(animFadein);
				yes.setVisibility(0);
				no.setVisibility(0);

				yes.startAnimation(animFadein);
				no.startAnimation(animFadein);
				break;
			case 4:
				welcome.setText("Are you a student?");
				welcome.startAnimation(animFadein);
				yes.startAnimation(animFadein);
				no.startAnimation(animFadein);
				break;
			case 5:
				welcome.setText("Would you like your phone to be silent at work?");
				welcome.startAnimation(animFadein);
				yes.startAnimation(animFadein);
				no.startAnimation(animFadein);
				break;
			default:
				Intent i = new Intent(this, MainActivity.class);
	            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
	        	startActivity(i);
				break;
		}
	}
	
	
	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) {     
	  super.onActivityResult(requestCode, resultCode, data); 
      if (resultCode == Activity.RESULT_OK) {
    	  screen++;
    	  updateScreen();
      }
	}
	
//	@Override
//	protected void onDestroy() {
//		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
//        getActionBar().show();
//	}
	
}
