package com.automates.automate;
// Based on http://wptrafficanalyzer.in/blog/locating-user-input-address-in-google-maps-android-api-v2-with-geocoding-api/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
 
import org.json.JSONObject;
 
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
 
import com.automates.automate.locations.GPSTracker;
import com.automates.automate.locations.GeocodeJSONParser;
import com.automates.automate.locations.UserLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
 
public class LocationActivity extends FragmentActivity {
 
    Button mBtnFind;
    GoogleMap mMap;
    EditText etPlace;
    EditText etRadius;
    EditText etName;
    Button btnAdd;
    ProgressBar loadingSpinner;
    
    Float radius_to_parse;
    Float radius;
    LatLng latLng;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_activity);
 
        // Getting reference to the buttons
        mBtnFind = (Button) findViewById(R.id.btn_show);
        btnAdd = (Button) findViewById(R.id.btn_add);
        
        // Getting reference to EditText
        etPlace = (EditText) findViewById(R.id.et_place);
        etRadius = (EditText) findViewById(R.id.et_radius);
        etName = (EditText) findViewById(R.id.et_name);
        
        // Getting reference to ProgressBar
        loadingSpinner = (ProgressBar) findViewById(R.id.loadingSpinner);
 
        // Getting reference to the SupportMapFragment
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        
        // Getting reference to the Google Map
        mMap = mapFragment.getMap();
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        
        // Try getting Map to current location
        
        GPSTracker gps = new GPSTracker(this);
        Location loc = gps.getLocation();
        
        LatLng currentLoc = new LatLng(loc.getLatitude(), loc.getLongitude());
        
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 15));


 
        // Setting click event listener for the find button
        mBtnFind.setOnClickListener(new OnClickListener() {
        	
            @Override
            public void onClick(View v) {
                // Getting the place entered
                String location = etPlace.getText().toString();
 
                if(location==null || location.equals("")){
                    Toast.makeText(getBaseContext(), "No Location is entered", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                // Getting the radius entered
                String r = etRadius.getText().toString();
                
                if(r==null || r.equals("")){
                    Toast.makeText(getBaseContext(), "No Radius is entered", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                try {
                	radius_to_parse = Float.parseFloat(r);
                } catch (NumberFormatException e) {
                	Toast.makeText(getBaseContext(), "Radius must be a number", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                // Show loading icon
                loadingSpinner.setVisibility(View.VISIBLE);
                
                // Getting the location entered with api
                String url = "https://maps.googleapis.com/maps/api/geocode/json?";
 
                try {
                    // encoding special characters like space in the user input place
                    location = URLEncoder.encode(location, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
 
                String address = "address=" + location;
 
                String sensor = "sensor=false";
 
                // url , from where the geocoding data is fetched
                url = url + address + "&" + sensor;
 
                // Instantiating DownloadTask to get places from Google Geocoding service
                // in a non-ui thread
                DownloadTask downloadTask = new DownloadTask();
 
                // Start downloading the geocoding places
                downloadTask.execute(url);
            }
        });
        
        // Setting click event listener for the add button
        btnAdd.setOnClickListener(new OnClickListener() {
        	
            @Override
            public void onClick(View v) {
                
            	// Check if a place has been found
            	if (latLng == null | radius == null) {
            		Toast.makeText(getBaseContext(), "No Location has been searched yet", Toast.LENGTH_SHORT).show();
                    return;
            	}
            	
            	// Get the name
                String name = etName.getText().toString();
 
                if(name==null || name.equals("")){
                    Toast.makeText(getBaseContext(), "No Name is entered", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                UserLocation userloc = new UserLocation(name, latLng, radius);
                
            }
        });
        
        
        
    }
 
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
 
            // Connecting to url
            urlConnection.connect();
 
            // Reading data from url
            iStream = urlConnection.getInputStream();
 
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
 
            StringBuffer sb = new StringBuffer();
 
            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }
 
            data = sb.toString();
            br.close();
 
        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
 
        return data;
    }
    /** A class, to download Places from Geocoding webservice */
    private class DownloadTask extends AsyncTask<String, Integer, String>{
 
        String data = null;
 
        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try{
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }
 
        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result){
 
            // Instantiating ParserTask which parses the json data from Geocoding webservice
            // in a non-ui thread
            ParserTask parserTask = new ParserTask();
 
            // Start parsing the places in JSON format
            // Invokes the "doInBackground()" method of the class ParseTask
            parserTask.execute(result);
        }
    }
 
    /** A class to parse the Geocoding Places in non-ui thread */
    class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{
 
        JSONObject jObject;
 
        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String,String>> doInBackground(String... jsonData) {
 
            List<HashMap<String, String>> places = null;
            GeocodeJSONParser parser = new GeocodeJSONParser();
 
            try{
                jObject = new JSONObject(jsonData[0]);
 
                /** Getting the parsed data as a an ArrayList */
                places = parser.parse(jObject);
 
            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return places;
        }
 
        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String,String>> list){
        	// Hide  loading icon
            loadingSpinner.setVisibility(View.INVISIBLE);
            // Clears all the existing markers
            mMap.clear();
            if (list.size() == 0) {
            	Toast.makeText(getBaseContext(), "Location not found", Toast.LENGTH_SHORT).show();
                return;
            }
            for(int i=0;i<list.size();i++){

                // Locate the first location
                if(i==0) {
                	 
                    // Creating a marker
                    MarkerOptions markerOptions = new MarkerOptions();
     
                    // Getting a place from the places list
                    HashMap<String, String> hmPlace = list.get(i);
     
                    // Getting latitude of the place
                    double lat = Double.parseDouble(hmPlace.get("lat"));
     
                    // Getting longitude of the place
                    double lng = Double.parseDouble(hmPlace.get("lng"));
     
                    // Getting name
                    String name = hmPlace.get("formatted_address");
     
                    latLng = new LatLng(lat, lng);
     
                    // Setting the position for the marker
                    markerOptions.position(latLng);
     
                    // Setting the title for the marker
                    markerOptions.title(name);
     
                    // Placing a marker on the position
                    mMap.addMarker(markerOptions);
                    int stroke = Color.argb(255, 137, 180, 215);
                    int fill = Color.argb(50, 137, 180, 215);
                    
                	mMap.addCircle(new CircleOptions()
                    	.center(latLng)
                    	.radius(radius_to_parse)
                    	.strokeColor(stroke)
                    	.fillColor(fill));
                	
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    
                    radius = radius_to_parse;
                }
            }
        }
    }
 
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
}