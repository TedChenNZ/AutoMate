package com.automates.automate.sqlite;

 
import java.util.LinkedList;
import java.util.List;
 
import com.automates.automate.pattern.Pattern;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This class manages the CRUD operations for the SQLite Database stored on the phone, it's a specific implementation for these patterns.
 * @author Dhanish
 *
 */
public class PatternDB extends SQLiteOpenHelper implements PatternManager{
 
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    public static final String DATABASE_NAME = "PatternDB";
    // For logging purposes - tag
    private final static String TAG = "PatternDB";
 
    public PatternDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);  
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create pattern table
        String CREATE_PATTERN_TABLE = "CREATE TABLE patterns ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
                "eventCategory TEXT, "+
                "event TEXT, "+
                "time INTEGER, "+
                "actualTime INTEGER, "+
                "day TEXT, "+
                "location TEXT, "+
                "wifi TEXT, "+
                "data INTEGER, "+
                "weekWeight REAL, "+
                "weight REAL, "+
                "statusCode INTEGER )";
 
        // create patterns table
        db.execSQL(CREATE_PATTERN_TABLE);
        Log.d(TAG, "database created");
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older patterns table if existed
        db.execSQL("DROP TABLE IF EXISTS patterns");
 
        // create fresh patterns table
        this.onCreate(db);
        Log.d(TAG, "database upgraded");
    }
    //---------------------------------------------------------------------
 
    /**
     * CRUD operations (create "add", read "get", update, delete) pattern + get all patterns + delete all patterns
     */
 
    // Patterns table name
    private static final String TABLE_PATTERNS = "patterns";
 
    // Patterns Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_EVENTCATEGORY = "eventCategory";
    private static final String KEY_EVENT = "event";
    private static final String KEY_TIME = "time";
    private static final String KEY_ACTUALTIME = "actualTIme";
    private static final String KEY_DAY = "day";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_WIFI = "wifi";
    private static final String KEY_DATA = "data";
    private static final String KEY_WEEKWEIGHT = "weekWeight";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_STATUSCODE = "statusCode";
    
    
    
    private static final String[] COLUMNS = {KEY_ID,KEY_EVENTCATEGORY,KEY_EVENT,KEY_TIME,KEY_ACTUALTIME,KEY_DAY,KEY_LOCATION,KEY_WIFI,KEY_DATA,KEY_WEEKWEIGHT, KEY_WEIGHT,KEY_STATUSCODE};
 
    public void addPattern(Pattern pattern){
        Log.d("addPattern", pattern.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_EVENTCATEGORY, pattern.getEventCategory()); 
        values.put(KEY_EVENT, pattern.getEvent());
        values.put(KEY_TIME, pattern.getTime());
        values.put(KEY_ACTUALTIME, pattern.getActualTime());
        values.put(KEY_DAY, pattern.getDay());
        values.put(KEY_LOCATION, pattern.getLocation());
        values.put(KEY_WIFI, pattern.getWifi());
        values.put(KEY_DATA, pattern.getData());
        values.put(KEY_WEEKWEIGHT, pattern.getWeekWeight());
        values.put(KEY_WEIGHT, pattern.getWeight());
        values.put(KEY_STATUSCODE, pattern.getStatusCode());
 
        // 3. insert
        db.insert(TABLE_PATTERNS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
 
        // 4. close
        db.close(); 
        
    }
 
    public Pattern getPattern(int id){
 
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
 
        // 2. build query
        Cursor cursor = 
                db.query(TABLE_PATTERNS, // a. table
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
 
        // 4. build pattern object
        Pattern pattern = new Pattern();
        pattern.setId(Integer.parseInt(cursor.getString(0)));
        pattern.setEventCategory(cursor.getString(1));
        pattern.setEvent(cursor.getString(2));
        pattern.setTime(Integer.parseInt(cursor.getString(3)));
        pattern.setActualTime(Long.parseLong(cursor.getString(4)));
        pattern.setDay(Integer.parseInt(cursor.getString(5)));
        pattern.setLocation(cursor.getString(6));
        pattern.setWifi(cursor.getString(7));
        pattern.setData(cursor.getString(8));
        pattern.setWeekWeight(Double.parseDouble(cursor.getString(9)));
        pattern.setWeight(Double.parseDouble(cursor.getString(10)));
        pattern.setStatusCode(Integer.parseInt(cursor.getString(11)));
 
        Log.d("TAG", "getPattern("+id+") " + pattern.toString());
 
        // 5. return pattern
        return pattern;
    }
 
    // Get All Patterns
    public List<Pattern> getAllPatterns() {
        List<Pattern> patterns = new LinkedList<Pattern>();
 
        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_PATTERNS;
 
        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
 
        // 3. go over each row, build pattern and add it to list
        Pattern pattern = null;
        if (cursor.moveToFirst()) {
            do {
                pattern = new Pattern();
                pattern.setId(Integer.parseInt(cursor.getString(0)));
                pattern.setEventCategory(cursor.getString(1));
                pattern.setEvent(cursor.getString(2));
                pattern.setTime(Integer.parseInt(cursor.getString(3)));
                pattern.setActualTime(Long.parseLong(cursor.getString(4)));
                pattern.setDay(Integer.parseInt(cursor.getString(5)));
                pattern.setLocation(cursor.getString(6));
                pattern.setWifi(cursor.getString(7));
                pattern.setData(cursor.getString(8));
                pattern.setWeekWeight(Double.parseDouble(cursor.getString(9)));
                pattern.setWeight(Double.parseDouble(cursor.getString(10)));
                pattern.setStatusCode(Integer.parseInt(cursor.getString(11)));
 
                // Add pattern to patterns
                patterns.add(pattern);
            } while (cursor.moveToNext());
        }
 
        Log.d("TAG", "getAllPatterns() " + patterns.toString());
 
        // return patterns
        return patterns;
    }
 
     // Updating single pattern
    public int updatePattern(Pattern pattern) {
 
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_EVENTCATEGORY, pattern.getEventCategory()); 
        values.put(KEY_EVENT, pattern.getEvent());
        values.put(KEY_TIME, pattern.getTime());
        values.put(KEY_ACTUALTIME, pattern.getActualTime());
        values.put(KEY_DAY, pattern.getDay());
        values.put(KEY_LOCATION, pattern.getLocation());
        values.put(KEY_WIFI, pattern.getWifi());
        values.put(KEY_DATA, pattern.getData());
        values.put(KEY_WEEKWEIGHT, pattern.getWeekWeight());
        values.put(KEY_WEIGHT, pattern.getWeight());
        values.put(KEY_STATUSCODE, pattern.getStatusCode());
 
        // 3. updating row
        int i = db.update(TABLE_PATTERNS, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(pattern.getId()) }); //selection args
 
        // 4. close
        db.close();
 
        Log.d("TAG", "updatePattern " + values.toString());
        return i;
 
    }
 
    // Deleting single pattern
    public void deletePattern(Pattern pattern) {
 
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // 2. delete
        db.delete(TABLE_PATTERNS,
                KEY_ID+" = ?",
                new String[] { String.valueOf(pattern.getId()) });
 
        // 3. close
        db.close();
 
        Log.d("TAG", "deletePattern " + pattern.toString());
 
    }
}