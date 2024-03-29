package com.automates.automate.sqlite;

 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.automates.automate.model.Routine;

import java.util.LinkedList;
import java.util.List;

/**
 * This class manages the CRUD operations for the SQLite Database stored on the phone, it's a specific implementation for these routines.
 * @author Dhanish
 *
 */
public class RoutineDB extends SQLiteOpenHelper implements RoutineManager{
 
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    public static final String DATABASE_NAME = "RoutineDB";
    // For logging purposes - tag
    private final static String TAG = "RoutineDB";
 
    public RoutineDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);  
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create routine table
        String CREATE_ROUTINE_TABLE = "CREATE TABLE routines ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
                "name TEXT, "+
                "eventCategory TEXT, "+
                "event TEXT, "+
                "hour INTEGER, "+
                "minute INTEGER, "+
                "day TEXT, "+
                "location TEXT, "+
                "wifi TEXT, "+
                "data INTEGER, "+
                "statusCode INTEGER )";
 
        // create routines table
        db.execSQL(CREATE_ROUTINE_TABLE);
        Log.d(TAG, "database created");
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older routines table if existed
        db.execSQL("DROP TABLE IF EXISTS routines");
 
        // create fresh routines table
        this.onCreate(db);
        Log.d(TAG, "database upgraded");
    }
    //---------------------------------------------------------------------
 
    /**
     * CRUD operations (create "add", read "get", update, delete) routine + get all routines + delete all routines
     */
 
    // Routines table name
    private static final String TABLE_ROUTINES = "routines";
 
    // Routines Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EVENTCATEGORY = "eventCategory";
    private static final String KEY_EVENT = "event";
    private static final String KEY_HOUR = "hour";
    private static final String KEY_MINUTE = "minute";
    private static final String KEY_DAY = "day";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_WIFI = "wifi";
    private static final String KEY_DATA = "data";
    private static final String KEY_STATUSCODE = "statusCode";
    
    
    
    private static final String[] COLUMNS = {KEY_ID,KEY_NAME, KEY_EVENTCATEGORY,KEY_EVENT,KEY_HOUR,KEY_MINUTE,KEY_DAY,KEY_LOCATION,KEY_WIFI,KEY_DATA,KEY_STATUSCODE};
 
    /* (non-Javadoc)
     * @see com.automates.automate.sqlite.RoutineManager#addRoutine(com.automates.automate.model.Routine)
     */
    @Override
    public Routine addRoutine(Routine routine){
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, routine.getName());
        values.put(KEY_EVENTCATEGORY, routine.getEventCategory()); 
        values.put(KEY_EVENT, routine.getEvent());
        values.put(KEY_HOUR, routine.getHour());
        values.put(KEY_MINUTE, routine.getMinute());
        values.put(KEY_DAY, routine.getDay());
        values.put(KEY_LOCATION, routine.getLocation());
        values.put(KEY_WIFI, routine.getWifi());
        values.put(KEY_DATA, routine.getmData());
        values.put(KEY_STATUSCODE, routine.getStatusCode());
 
        // 3. insert
        long id = db.insert(TABLE_ROUTINES, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
 
        // 4. close
        db.close(); 
        routine.setId((int) id);
        return routine;
    }
 
    /* (non-Javadoc)
     * @see com.automates.automate.sqlite.RoutineManager#getRoutine(int)
     */
    @Override
    public Routine getRoutine(int id){
 
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
 
        // 2. build query
        Cursor cursor = 
                db.query(TABLE_ROUTINES, // a. table
                COLUMNS, // b. column names
                " id = ?", // c. selections 
                new String[] { String.valueOf(id) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
 
        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();
 
        // 4. build routine object
        Routine routine = new Routine();
        routine.setId(cursor.getInt(0));
        routine.setName(cursor.getString(1));
        routine.setEventCategory(cursor.getString(2));
        routine.setEvent(cursor.getString(3));
        routine.setHour(cursor.getInt(4));
        routine.setMinute(cursor.getInt(5));
        routine.setDay(cursor.getString(6));
        routine.setLocation(cursor.getString(7));
        routine.setWifi(cursor.getString(8));
        routine.setmData(cursor.getString(9));
        routine.setStatusCode(cursor.getInt(10));
 
        Log.d(TAG, "getRoutine("+id+") " + routine.toString());
 
        // 5. return routine
        return routine;
    }
 
    // Get All routines
    /* (non-Javadoc)
     * @see com.automates.automate.sqlite.RoutineManager#getAllRoutines()
     */
    @Override
    public List<Routine> getAllRoutines() {
        List<Routine> routines = new LinkedList<Routine>();
 
        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_ROUTINES;
 
        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
 
        // 3. go over each row, build routine and add it to list
        Routine routine = null;
        if (cursor.moveToFirst()) {
            do {
                routine = new Routine();
                routine.setId(cursor.getInt(0));
                routine.setName(cursor.getString(1));
                routine.setEventCategory(cursor.getString(2));
                routine.setEvent(cursor.getString(3));
                routine.setHour(cursor.getInt(4));
                routine.setMinute(cursor.getInt(5));
                routine.setDay(cursor.getString(6));
                routine.setLocation(cursor.getString(7));
                routine.setWifi(cursor.getString(8));
                routine.setmData(cursor.getString(9));
                routine.setStatusCode(cursor.getInt(10));
 
                // Add routine to routines
                routines.add(routine);
            } while (cursor.moveToNext());
        }
 
//        Log.d(TAG, "getAllRoutines() " + routines.toString());
 
        // return routines
        return routines;
    }
 
     // Updating single routine
    /* (non-Javadoc)
     * @see com.automates.automate.sqlite.RoutineManager#updateRoutine(com.automates.automate.model.Routine)
     */
    @Override
    public int updateRoutine(Routine routine) {
 
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, routine.getName());
        values.put(KEY_EVENTCATEGORY, routine.getEventCategory()); 
        values.put(KEY_EVENT, routine.getEvent());
        values.put(KEY_HOUR, routine.getHour());
        values.put(KEY_MINUTE, routine.getMinute());
        values.put(KEY_DAY, routine.getDay());
        values.put(KEY_LOCATION, routine.getLocation());
        values.put(KEY_WIFI, routine.getWifi());
        values.put(KEY_DATA, routine.getmData());
        values.put(KEY_STATUSCODE, routine.getStatusCode());
 
        // 3. updating row
        int i = db.update(TABLE_ROUTINES, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(routine.getId()) }); //selection args
 
        // 4. close
        db.close();
 
        Log.d(TAG, "updateRoutine " + values.toString());
        return i;
 
    }
 
    // Deleting single routine
    /* (non-Javadoc)
     * @see com.automates.automate.sqlite.RoutineManager#deleteRoutine(com.automates.automate.model.Routine)
     */
    @Override
    public void deleteRoutine(Routine routine) {
 
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // 2. delete
        db.delete(TABLE_ROUTINES,
                KEY_ID+" = ?",
                new String[] { String.valueOf(routine.getId()) });
 
        // 3. close
        db.close();
 
        Log.d(TAG, "deleteRoutine " + routine.toString());
 
    }
}