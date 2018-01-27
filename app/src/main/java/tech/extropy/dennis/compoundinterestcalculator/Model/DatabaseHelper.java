package tech.extropy.dennis.compoundinterestcalculator.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by dennis on 1/24/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String TABLE_NAME = "people_table";
    private static final String COL1 = "ID";
    private static final String COL2 = "name";

    public DatabaseHelper(Context context){
        //Factory is SQLiteDatabase.CursorFactory
        super(context, TABLE_NAME, null, 1);
    }

    //When the table is created for the first time.
    @Override
    public void onCreate(SQLiteDatabase db){
        //CREATE TABLE people_table (ID INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 +" TEXT)";
        //execSQL: Execute a single SQL statement
        //that is NOT a SELECT or any other SQL statement that returns data.
        db.execSQL(createTable);
    }

    //onUpgrade(): Is called when the database file exists but the stored version
    //number is lower than requested in constructor. The onUpgrade()
    //should update the table schema to the requested version.
    @Override
    public void onUpgrade(SQLiteDatabase db,int i, int i1){
        //Drop people_table if existed.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String item) {
        /*Create and/or open a database that will be used for reading and writing.
        The first time this is called, the database will be opened and
        onCreate(SQLiteDatabase), onUpgrade(SQLiteDatabase, int, int) and/or onOpen(SQLiteDatabase)
        will be called. */
        SQLiteDatabase db = this.getWritableDatabase();

        //This class is used to store a set of values that the ContentResolver can process.
        ContentValues contentValues = new ContentValues();

        //name that maps to a String item.
        contentValues.put(COL2, item);

        Log.d(TAG, "addData: Adding " + item + " to " + TABLE_NAME);

        //insert: Convenience method for inserting a row into the database.
        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns all the data from database
     * @return
     */
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Returns only the ID that matches the name passed in
     * @param name
     * @return
     */
    public Cursor getItemID(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME +
                " WHERE " + COL2 + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

}