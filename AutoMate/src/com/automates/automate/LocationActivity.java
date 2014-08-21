package com.automates.automate;
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
 



import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
 



import com.automates.automate.locations.GeocodeJSONParser;
import com.automates.automate.locations.UserLocation;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
 
public class LocationActivity extends FragmentActivity {
 
    private com.beardedhen.androidbootstrap.BootstrapButton mBtnFind;
    private GoogleMap mMap;
    private EditText etPlace;
    private EditText etRadius;
    private EditText etName;
    private com.beardedhen.androidbootstrap.BootstrapButton btnAdd;
    private RelativeLayout loading;
    
    private UserLocation userloc = new UserLocation();
    
    private CircleOptions radius;
    private MarkerOptions markerOptions;
    
    int EditItem;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
 
        // Getting reference to the buttons
        mBtnFind = (com.beardedhen.androidbootstrap.BootstrapButton) findViewById(R.id.btn_show);
        btnAdd = (com.beardedhen.androidbootstrap.BootstrapButton) findViewById(R.id.btn_add);
        
        // Getting reference to EditText
        etPlace = (EditText) findViewById(R.id.et_place);
        etRadius = (EditText) findViewById(R.id.et_radius);
        etName = (EditText) findViewById(R.id.et_name);
        
        // Getting reference to ProgressBar
        loading = (RelativeLayout) findViewById(R.id.loadingLayout);
 
        // Getting reference to the SupportMapFragment
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        
        // Getting reference to the Google Map
        mMap = mapFragment.getMap();
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setMyLocationEnabled(true);
//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        
        
        
        // Try getting Map to current location
        Intent intent = getIntent();
        EditItem = intent.getIntExtra("EditItem", -1);
        String FIRSTRUN = intent.getStringExtra("FIRSTRUN");
        if (FIRSTRUN != null) {
        	etName.setText(FIRSTRUN);
        }
        if (EditItem != -1) {
        	userloc = PhoneState.getLocationsList().get(EditItem);
        	
        	showUserLocation(userloc, mMap, 15);
        	showRadius(userloc, mMap);
        	etPlace.setText(userloc.getLocationName());
        	etRadius.setText(""+userloc.getRadius());
        	etName.setText(userloc.getName());
            
        } else {
//	        GPSTracker gps = PhoneState.getGPSTracker();
//	        Location loc = gps.getLastLocation();
//	        LatLng currentLoc = new LatLng(loc.getLatitude(), loc.getLongitude());
//	        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 15));
	        
        }

        // Setting click event listener for the save button
        btnAdd.setOnClickListener(new saveListener());
        
        // Setting click event listener for the find button
        mBtnFind.setOnClickListener(new OnClickListener() {
        	
            @Override
            public void onClick(View v) {
            	hideSoftKeyboard();
                // Getting the place entered
                String location = etPlace.getText().toString();
 
                if(location==null || location.equals("")){
                    Toast.makeText(getBaseContext(), "No Location is entered", Toast.LENGTH_SHORT).show();
                    return;
                }
                
//                // Getting the radius entered
//                String r = etRadius.getText().toString();
//                
//                if(r==null || r.equals("")){
//                    Toast.makeText(getBaseContext(), "No Radius is entered", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                
//                try {
//                	radius_to_parse = Integer.parseInt(r);
//                } catch (NumberFormatException e) {
//                	Toast.makeText(getBaseContext(), "Radius must be a number", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                
                // Show loading icon
                loading.setVisibility(View.VISIBLE);
                
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
        
        // Radius update radius on text change
        etRadius.addTextChangedListener(new TextWatcher() {
        	@Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                String string = etRadius.getText().toString();
                if (string != null && string.length() > 0 ) {
                	userloc.setRadius(Integer.parseInt(string));
                }
                showRadius(userloc, mMap);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        	
        });
        

    }
    

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        menu.findItem(R.id.action_settings).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }
    
    private void showRadius(UserLocation ul, GoogleMap gm) {
    	
    	if (ul.getRadius() != null && markerOptions != null && markerOptions.getPosition() != null) {
	        int stroke = Color.argb(255, 137, 180, 215);
	        int fill = Color.argb(100, 137, 180, 215);
	        gm.clear();
	        
	        
	        radius = new CircleOptions()
		        .center(markerOptions.getPosition())
		        .radius(ul.getRadius())
		        .strokeColor(stroke)
		        .fillColor(fill);
	        gm.addMarker(markerOptions);
	        gm.addCircle(radius);
    	}
    }

    /**
     * Moves camera to userLocation ul
     * @param ul
     * @param gm
     * @param zoom
     */
    private void showUserLocation(UserLocation ul, GoogleMap gm, int zoom) {
        // Creating a marker
        markerOptions = new MarkerOptions();
        // Setting the position for the marker
        markerOptions.position(ul.getLocation());

        // Setting the title for the marker
        markerOptions.title(ul.getLocationName());

        // Placing a marker on the position
        gm.addMarker(markerOptions);


        //        gm.animateCamera(CameraUpdateFactory.newLatLng(ul.getLocation()));
        gm.animateCamera(CameraUpdateFactory.newLatLngZoom(ul.getLocation(), zoom));

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
        	loading.setVisibility(View.INVISIBLE);
            // Clears all the existing markers
            mMap.clear();
            if (list.size() == 0) {
            	Toast.makeText(getBaseContext(), "Location not found", Toast.LENGTH_SHORT).show();
                return;
            }
            for(int i=0;i<list.size();i++){

                // Locate the first location
                if(i==0) {

     
                    // Getting a place from the places list
                    HashMap<String, String> hmPlace = list.get(i);
     
                    // Getting latitude of the place
                    double lat = Double.parseDouble(hmPlace.get("lat"));
     
                    // Getting longitude of the place
                    double lng = Double.parseDouble(hmPlace.get("lng"));
     
                    // Getting name
                    String name = hmPlace.get("formatted_address");
                    
                    // Update userloc
                    userloc.setLocation(new LatLng(lat, lng));
//                    userloc.setRadius(radius_to_parse);
                    userloc.setLocationName(name);
                    
                    
                    showUserLocation(userloc, mMap, 15);
                    showRadius(userloc, mMap);
                    
                    
                }
            }
        }
    }
    
    /**
     * Hides the soft keyboard
     */
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
    
    /**
     *Listener for add button
     */
    private class saveListener implements OnClickListener {
    	
        @Override
        public void onClick(View v) {
        	hideSoftKeyboard();
        	
        	// Get the name
            String name = etName.getText().toString().trim();

            if(name==null || name.equals("")){
                Toast.makeText(getBaseContext(), "No Name is entered", Toast.LENGTH_SHORT).show();
                return;
            }
            for (UserLocation u: PhoneState.getLocationsList()) {
            	Log.d("LocationActivity", u.getName() + " " + name);
            	if (u.getName().equals(name)) {
                    Toast.makeText(getBaseContext(), "The Name '" + name + "' is already in use", Toast.LENGTH_SHORT).show();
                    return;
            	}
            }
            
        	// Check if a place has been found
        	if (userloc.getLocation() == null) {
        		Toast.makeText(getBaseContext(), "No Location has been searched yet", Toast.LENGTH_SHORT).show();
                return;
        	}
        	
        	// Check radius
        	if (userloc.getRadius() == null || userloc.getRadius() == 0) {
        		Toast.makeText(getBaseContext(), "Radius must be greater than 0", Toast.LENGTH_SHORT).show();
                return;
        	}

            
            userloc.setName(name);
            if (EditItem != -1) {
            	PhoneState.getLocationsList().set(EditItem, userloc);
            } else {
            	PhoneState.getLocationsList().add(userloc);
            }
            
            // Return to previous activity
        	Intent resultIntent = new Intent();
        	setResult(Activity.RESULT_OK, resultIntent);
        	finish();
            
        }
    }
    

}