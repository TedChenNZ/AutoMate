package com.automates.automate;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class RoutineActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_location_activity);
//	Routine r = new Routine(10, "tr1", "false", "Wifi", 22, 17, "", "", "", "", 1);
//	PhoneState.getRoutineDb().addRoutine(r);
	notification("Pattern recognised!", "Tap this notification to configure your routine options.", 3);
	

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
    }
}
