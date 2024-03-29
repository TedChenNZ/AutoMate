package com.automates.automate.service.location;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import com.automates.automate.service.PhoneService;

// Based on http://www.androidhive.info/2012/07/android-gps-location-manager-tutorial/

/**
 * Service to track the location of the device
 */
public class LocationTrackerService extends Service implements LocationListener {
	private final static String TAG = "GPSTracker";
    private static boolean instantiated = false;
	private static LocationTrackerService instance = null;

    private static Context mContext;

    // flag for GPS status
    private boolean isGPSEnabled = false;
 
    // flag for network status
    private boolean isNetworkEnabled = false;
 
    // flag for GPS status
    private boolean canGetLocation = false;
 
    private Location lastLocation; // location
    private double latitude; // latitude
    private double longitude; // longitude
 
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 200; // meters
 
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 5; // 5 minutes
 
    // Declaring a Location Manager
    protected static LocationManager locationManager;

    private LocationTrackerService() {
    }

    /**
     * Singleton getter
     * @return
     */
    public static LocationTrackerService getInstance() {
        if (instance == null) {
            instance = new LocationTrackerService();
        }
        return instance;
    }

    /**
     * Initialize the service with a context
     * @param context
     */
    public static void init(Context context) {
        mContext = context;
        locationManager = (LocationManager) mContext
                .getSystemService(LOCATION_SERVICE);
        instantiated = true;
    }

    /**
     * Check if this service is already isntantiated
     * @return
     */
    public static boolean isInstantiated() {
        return instantiated;
    }

    /**
     * Get current location using the network provider
     * @return
     */
    private Location getLocationNetworkProvider() {
    	try {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            if (locationManager != null) {
                lastLocation = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (lastLocation != null) {
                    latitude = lastLocation.getLatitude();
                    longitude = lastLocation.getLongitude();
                }
            }
    	} catch (Exception e) {
    		
    	}
    	return lastLocation;
    }

    /**
     * Get current location using GPS
     * @return
     */
    private Location getLocationGPS() {
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        Log.d(TAG, "getLocationGPS");
        if (locationManager != null) {
            lastLocation = locationManager
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                latitude = lastLocation.getLatitude();
                longitude = lastLocation.getLongitude();
            }
        }
        return lastLocation;
    }

    /**
     * Get the current location
     * @return
     */
    public Location getLocation() {
        try {
            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
 
            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
 
            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                	getLocationNetworkProvider();
                }
                // else if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {

                	if (lastLocation == null) {
                		getLocationGPS();
                	}
                }
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        return lastLocation;
    }
     
    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     * */
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(LocationTrackerService.this);
        }       
    }
     
    /**
     * Function to get latitude
     * */
    public double getLatitude(){
        if(lastLocation != null){
            latitude = lastLocation.getLatitude();
        }
         
        // return latitude
        return latitude;
    }
     
    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        if(lastLocation != null){
            longitude = lastLocation.getLongitude();
        }
         
        // return longitude
        return longitude;
    }
     
    /**
     * Function to check GPS/wifi enabled
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }
     
    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
      
        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");
  
        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
  
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
  
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
  
        // Showing Alert Message
        alertDialog.show();
    }

    /**
     * Update PhoneService on location change
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
    	PhoneService.getInstance().update(mContext);
    }
 
    @Override
    public void onProviderDisabled(String provider) {
    }
 
    @Override
    public void onProviderEnabled(String provider) {
    }
 
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
 
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

	public Location getLastLocation() {
		return lastLocation;
	}
}
 