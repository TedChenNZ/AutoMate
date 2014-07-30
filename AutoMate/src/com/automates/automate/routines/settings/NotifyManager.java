package com.automates.automate.routines.settings;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.automates.automate.R;
import com.automates.automate.RoutineActivity;

public class NotifyManager extends Activity {

	public NotifyManager(){}
	

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

}
