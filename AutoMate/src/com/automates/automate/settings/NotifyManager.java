package com.automates.automate.settings;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.automates.automate.PhoneState;
import com.automates.automate.R;
import com.automates.automate.RoutineActivity;
import com.automates.automate.pattern.Pattern;
import com.automates.automate.pattern.StatusCode;

public class NotifyManager extends Activity {

    NotificationManager mNotificationManager;

    public NotifyManager(){}

    @Override
    protected void onCreate(Bundle saved) {
        super.onCreate(saved);
        String ns = Context.NOTIFICATION_SERVICE;
        mNotificationManager = (NotificationManager) getSystemService(ns);
    }

    @SuppressWarnings("deprecation")
    public void notification(String subject, String body, int routineID, int patternID){
		Context context = PhoneState.getContext();
		int NOT_ID = 1;
		int icon = R.drawable.ic_launcher;
		CharSequence tickerText = "Automate - Routine recognised!";
		long when = System.currentTimeMillis();

		int requestID = (int) System.currentTimeMillis();
		Notification notification = new Notification(icon, tickerText, when);
	//	Context context = getApplicationContext();
		Pattern p = PhoneState.getPatternDb().getPattern(patternID);
		p.setStatusCode(StatusCode.AWAITING_APPROVAL);
		PhoneState.getPatternDb().updatePattern(p);
	
		Intent notificationIntent = new Intent(PhoneState.getContext(), RoutineActivity.class);
		notificationIntent.putExtra("routineID", routineID);
		notificationIntent.putExtra("patternID", patternID);
		PendingIntent contentIntent = PendingIntent.getActivity(PhoneState.getContext(), requestID, notificationIntent, 0);
		notificationIntent.setData((Uri.parse("mystring"+requestID)));
		notification.setLatestEventInfo(context, subject, body, contentIntent);
//		notification.flags += Notification.FLAG_ONGOING_EVENT;
		notification.flags += Notification.FLAG_AUTO_CANCEL;
		if(mNotificationManager == null){
		    mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		}
		mNotificationManager.notify("tag", NOT_ID, notification);
    }

}
