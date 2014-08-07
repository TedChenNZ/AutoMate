package com.automates.automate.sqlite;

import java.util.ArrayList;
import java.util.List;

import com.automates.automate.locations.UserLocation;
import com.google.android.gms.maps.model.LatLng;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserLocationsSQLiteDBManager extends SQLiteOpenHelper implements UserLocationsManager {
	// Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    public static final String DATABASE_NAME = "UserLocationsDB";
    
    // Table Data
    private static final String TABLE_NAME = "user_locations";
    
    // Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_LAT = "latitude";
    private static final String KEY_LNG = "longitude";    
    private static final String KEY_RADIUS = "radius";
    private static final String KEY_LOCATION_NAME = "location_name";
 
    private static final String[] COLUMNS = {KEY_ID,KEY_NAME,KEY_LAT,KEY_LNG,KEY_RADIUS,KEY_LOCATION_NAME};
 
    public UserLocationsSQLiteDBManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);  
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create table
        String CREATE_USER_LOCATION_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
        		KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_NAME + " TEXT, " + 
                KEY_LAT + " REAL, " +
                KEY_LNG + " REAL, " +
                KEY_RADIUS + " INTEGER, " +
                KEY_LOCATION_NAME + " TEXT )";
 
        // create table
        db.execSQL(CREATE_USER_LOCATION_TABLE);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
 
        // create fresh table
        this.onCreate(db);
    }
    
    //---------------------------------------------------------------------
    
    /* (non-Javadoc)
     * @see com.automates.automate.sqlite.UserLocationsManager#add(com.automates.automate.locations.UserLocation)
     */

    
    @Override
    public UserLocation add(UserLocation ul){
        Log.d("addUserLocation", ul.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // 2. create ContentValues to add key "column"/value
        ContentValues values = userLocationToContentValues(ul);
 
        // 3. insert
        long id = db.insert(TABLE_NAME, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
        ul.setId((int) id);
        // 4. close
        db.close(); 
        
        return ul;
    }
 
    /* (non-Javadoc)
     * @see com.automates.automate.sqlite.UserLocationsManager#get(int)
     */
    @Override
    public UserLocation get(int id){
    	 
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
 
        // 2. build query
        Cursor cursor = 
                db.query(TABLE_NAME, // a. table
                COLUMNS, // b. column names
                " "+KEY_ID+" = ?", // c. selections 
                new String[] { String.valueOf(id) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
 
        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();
 
        // 4. build book object
        UserLocation ul = cursorToUserLocation(cursor);
        
        Log.d("getUserLocation("+id+")", ul.toString());
 
        // 5. return 
        return ul;
    }
    // Get All Books
    /* (non-Javadoc)
     * @see com.automates.automate.sqlite.UserLocationsManager#getAll()
     */
    @Override
    public List<UserLocation> getAll() {
        List<UserLocation> locationsList = new ArrayList<UserLocation>();
 
        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_NAME;
 
        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
 
        // 3. go over each row, build object and add it to list
        UserLocation ul = null;
        if (cursor.moveToFirst()) {
            do {
                ul = cursorToUserLocation(cursor);
 
                // Add to list
                locationsList.add(ul);
            } while (cursor.moveToNext());
        }
 
        Log.d("getAllUserLocations()", locationsList.toString());
 
        // return books
        return locationsList;
    }
 
     // Updating single object
    /* (non-Javadoc)
     * @see com.automates.automate.sqlite.UserLocationsManager#update(com.automates.automate.locations.UserLocation)
     */
    @Override
    public int update(UserLocation ul) {
 
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // 2. create ContentValues to add key "column"/value
        ContentValues values = userLocationToContentValues(ul);
 
        // 3. updating row
        int i = db.update(TABLE_NAME, //table
                values, // column/value
                KEY_ID + " = ?", // selections
                new String[] { String.valueOf(ul.getId()) }); //selection args
        Log.d("Update", ""+i);
 
        // 4. close
        db.close();
 
        return i;
 
    }
 
    // Deleting single UL
    /* (non-Javadoc)
     * @see com.automates.automate.sqlite.UserLocationsManager#remove(com.automates.automate.locations.UserLocation)
     */
    @Override
    public void remove(UserLocation ul) {
 
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // 2. delete
        db.delete(TABLE_NAME,
                KEY_ID+" = ?",
                new String[] { String.valueOf(ul.getId()) });
 
        // 3. close
        db.close();
 
//        Log.d("deleteBook", book.toString());
 
    }
 
    
    
    private ContentValues userLocationToContentValues (UserLocation ul) {
    	ContentValues values = new ContentValues();
//    	values.put(KEY_ID, ul.getId());
        values.put(KEY_NAME, ul.getName());
        values.put(KEY_LAT, ul.getLocation().latitude);
        values.put(KEY_LNG, ul.getLocation().longitude);
        values.put(KEY_RADIUS, ul.getRadius());
        values.put(KEY_LOCATION_NAME, ul.getLocationName());
        return values;
    }
    
    

    
    private UserLocation cursorToUserLocation(Cursor cursor) {
    	UserLocation ul = new UserLocation();
    	ul.setId(cursor.getInt(0));
    	ul.setName(cursor.getString(1));
    	LatLng loc = new LatLng(cursor.getDouble(2), cursor.getDouble(3));
    	ul.setLocation(loc);
    	ul.setRadius(cursor.getInt(4));
    	ul.setLocationName(cursor.getString(5));
    	return ul;
    }
}
