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
    private static final String TABLE_NAME = "interest_table";
    private static final String COL1 = "ID";
    private static final String COL2 = "file_name";
    private static final String COL3 = "years_to_grow";
    private static final String COL4 = "interest_rate";
    private static final String COL5 = "current_principle";
    private static final String COL6 = "annual_addition";
    private static final String COL7 = "number_of_time_compounded_annually";
    private static final String COL8 = "make_additions_end_or_start";

    public DatabaseHelper(Context context) {
        //Factory is SQLiteDatabase.CursorFactory
        super(context, TABLE_NAME, null, 1);
    }

    //When the table is created for the first time.
    @Override
    public void onCreate(SQLiteDatabase db) {
        //CREATE TABLE people_table (ID INTEGER PRIMARY KEY AUTOINCREMENT, YearsToGrow TEXT)
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 + " TEXT, " + COL3 + " INTEGER, " + COL4 + " REAL, " + COL5 + " REAL, " + COL6 + " REAL,"+
                COL7 + " INT,"+COL8+" INT)";

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

    //CompoundedInterestAnnualAddition
    public boolean addData(String fileName,double interestRate,int yearsToGrow,
                           double currentPrinciple,double annualAddition,int numberOfTimesCompounded,int startOrEnd) {
        /*Create and/or open a database that will be used for reading and writing.
        The first time this is called, the database will be opened and
        onCreate(SQLiteDatabase), onUpgrade(SQLiteDatabase, int, int) and/or onOpen(SQLiteDatabase)
        will be called. */
        SQLiteDatabase db = this.getWritableDatabase();

        //This class is used to store a set of values that the ContentResolver can process.
        ContentValues contentValues = new ContentValues();

        //name that maps to a String item.
        contentValues.put(COL2, fileName);
        contentValues.put(COL3, yearsToGrow);
        contentValues.put(COL4, interestRate);
        contentValues.put(COL5, currentPrinciple);
        contentValues.put(COL6, annualAddition);
        contentValues.put(COL7, numberOfTimesCompounded);
        contentValues.put(COL8, startOrEnd);

        //insert: Convenience method for inserting a row into the database.
        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    //CompoundedInterestAnnualAddition
    public boolean addData(String fileName,int yearsToGrow, double interestRate,
                           double currentPrinciple, int numberOfTimesCompoundedCompute) {
        /*Create and/or open a database that will be used for reading and writing.
        The first time this is called, the database will be opened and
        onCreate(SQLiteDatabase), onUpgrade(SQLiteDatabase, int, int) and/or onOpen(SQLiteDatabase)
        will be called. */
        SQLiteDatabase db = this.getWritableDatabase();

        //This class is used to store a set of values that the ContentResolver can process.
        ContentValues contentValues = new ContentValues();

        //name that maps to a String item.
        contentValues.put(COL2, fileName);
        contentValues.put(COL3, yearsToGrow);
        contentValues.put(COL4, interestRate);
        contentValues.put(COL5, currentPrinciple);
        contentValues.put(COL7, numberOfTimesCompoundedCompute);

        //insert: Convenience method for inserting a row into the database.
        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    //SimpleInterestActivity and ContinuouslyCompoundedActivity
    public boolean addData(String fileName, int yearsToGrow,
                           double interestRate, double currentPrinciple) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, fileName);
        contentValues.put(COL3, yearsToGrow);
        contentValues.put(COL4, interestRate);
        contentValues.put(COL5, currentPrinciple);

        Log.d(TAG, "addData: Adding " + fileName + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    //original
    public boolean addData(String item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COL2, item);

        Log.d(TAG, "addData: Adding " + item + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

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
        //Cursor: This interface provides random
        //read-write access to the result set returned by a database query.
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

    /**
     * Updates the name field
     * @param newName
     * @param id
     * @param oldName
     */
    public void updateName(String newName, int id, String oldName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 +
                " = '" + newName + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + oldName + "'";
        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting name to " + newName);
        db.execSQL(query);
    }

    /**
     * Delete from database
     * @param id
     * @param name
     */
    public void deleteName(int id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + name + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + name + " from database.");
        db.execSQL(query);
    }
}