package com.automates.automate.service.routine;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import com.automates.automate.service.PhoneService;
import com.automates.automate.model.Routine;
import com.automates.automate.service.model.RoutineService;
import com.automates.automate.service.pattern.StatusCode;
import com.automates.automate.service.pattern.WeightManager;
import com.automates.automate.service.settings.Data;
import com.automates.automate.service.settings.RingerProfiles;
import com.automates.automate.service.settings.Settings;
import com.automates.automate.service.settings.Wifi;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Service that applies Routines
 */
public class RoutineApplierService extends Service implements PropertyChangeListener{
    private static boolean instantiated = false;

	List<Routine> routines;
	public Context context;
	private final static String TAG = "RoutineApplierService";
	private final static long MIN_RECENT = WeightManager.timeDivision;
	
    private static RoutineApplierService instance = null;
    private RoutineApplierService(){}

    public static RoutineApplierService getInstance() {
        if (instance == null) {
            instance = new RoutineApplierService();
        }
        return instance;
    }

	public void init(Context context){
		PhoneService.getInstance().addChangeListener(this);
		this.context = context;
        startRoutineChecking(context);
        instantiated = true;
	}

    public static boolean isInstantiated() {
        return instantiated;
    }

    private void startRoutineChecking(Context context) {
        AlarmManager alarmManager=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TimelyChecker.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),30000,pendingIntent);
    }

    /**
     * Check routines if the phone has had a change of state
     * @param event
     */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		boolean routines = sharedPrefs.getBoolean("pref_routines", true);
		if (routines) {
			checkRoutines();
		};
	}

    /**
     * Check if a routine should be applied
     */
	public void checkRoutines(){
		routines = RoutineService.getInstance().getAllRoutines();
		if (routines != null) {
			Log.d(TAG, "Checking applications");
			for(Routine r : routines){
				if(!checkAppliedRecently(r) && checkPhoneConditions(r)){
					apply(r);
				}
			}
		}

	}

    /**
     * Apply a routine
     * @param r
     */
	private void apply(Routine r) {
		// TODO Auto-generated method stub
		LoggerService.getInstance().logRoutine(r.getId());
		Log.d(TAG, "Actioning routine " + r.getName());
		if(r.getEventCategory().equalsIgnoreCase(Settings.WIFI)){
			if(r.getEvent().equalsIgnoreCase("false")){
				Wifi.setWifiEnabled(context, false);
                Toast.makeText(context, "AutoMate: Wifi has been turned off", Toast.LENGTH_SHORT).show();
				Log.d(TAG, "Wifi turned off");
			}
			else{
				Wifi.setWifiEnabled(context, true);
				Log.d(TAG, "Wifi turned on");
                Toast.makeText(context, "AutoMate: Wifi has been turned on", Toast.LENGTH_SHORT).show();
			}
		}
		else if(r.getEventCategory().equalsIgnoreCase(Settings.RINGER)){
			RingerProfiles.setSoundProfile(context, Integer.parseInt(r.getEvent()));
			Log.d(TAG, "Ringer changed to " + r.getEvent());
            Toast.makeText(context, "AutoMate: Ringer has been changed to " + r.getEvent(), Toast.LENGTH_SHORT).show();
		}
		else if(r.getEventCategory().equalsIgnoreCase(Settings.MDATA)){
			if(r.getEvent().equalsIgnoreCase("false")){
				Data.setDataEnabled(context, false);
				Log.d(TAG, "Data turned off");
                Toast.makeText(context, "AutoMate: Data has been turned off", Toast.LENGTH_SHORT).show();
			}
			else{
				Data.setDataEnabled(context, true);
				Log.d(TAG, "Data turned on");
                Toast.makeText(context, "AutoMate: Data has been turned on", Toast.LENGTH_SHORT).show();
			}
		}
		

	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

    /**
     * Check if the conditions of the phone state match the triggers of a Routine
     * @param r
     * @return
     */
	private boolean checkPhoneConditions(Routine r){
		boolean conditions = true;

		Time time = new Time();
		time.setToNow();
		if (r.getEventCategory() == null || r.getEvent() == null || r.getDay() == null || r.getLocation() == null || r.getWifi() == null || r.getmData() == null) {
			return false;
		}

		if (!r.getDay().equals(StatusCode.EMPTY) && r.getDay().indexOf(time.weekDay) == -1){conditions = false;};
		if (r.getHour() != (StatusCode.DECLINED) && r.getHour() != time.hour){conditions = false;};
		if (r.getMinute() != (StatusCode.DECLINED) && r.getMinute() != time.minute){conditions = false;};
		if (!r.getLocation().equals(StatusCode.EMPTY) && !r.getLocation().equals(PhoneService.getInstance().getSetLocation())){conditions = false;};
		if (!r.getmData().equals(StatusCode.EMPTY) && !r.getmData().equals(Boolean.toString(PhoneService.getInstance().isDataEnabled()))){conditions = false;};
		if (!r.getWifi().equals(StatusCode.EMPTY) && !r.getWifi().equals(PhoneService.getInstance().getWifiBSSID())){conditions = false;};

		if(r.getStatusCode() != StatusCode.IMPLEMENTED){
			conditions = false;
		}

        if (r.getEventCategory().equals(Settings.RINGER)) {
            if (Integer.toString(PhoneService.getInstance().getSoundProfile()).equals(r.getEvent())) {
                conditions = false;
            }
        } else if (r.getEventCategory().equals(Settings.WIFI)) {
            if (Boolean.toString(PhoneService.getInstance().isWifiEnabled()).equals(r.getEvent())) {
                conditions = false;
            }
        } else if (r.getEventCategory().equals(Settings.MDATA)) {
            if (Boolean.toString(PhoneService.getInstance().isDataEnabled()).equals(r.getEvent())) {
                conditions = false;
            }
        }
		return conditions;
	}

    /**
     * Check if a routine has been applied recently
     * @param r
     * @return
     */
	private boolean checkAppliedRecently(Routine r) {
		boolean applied = false;
		if (LoggerService.getInstance().getAppliedRoutines().get(r.getId()) != null) {
            Log.d(TAG, ""+LoggerService.getInstance().getAppliedRoutines().get(r.getId()));

            if ((System.currentTimeMillis() - LoggerService.getInstance().getAppliedRoutines().get(r.getId())) < MIN_RECENT) {
				applied = true;
			}
		}
		return applied;
	}
	
}

