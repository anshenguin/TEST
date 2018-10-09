package com.kinitoapps.punjabgovttest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by HP INDIA on 17-Sep-18.
 */

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ggr.db";

    // Login table name
    private static final String TABLE_USER = "user";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_COURSE = "course";
    private static final String KEY_FIELD = "field";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_PERCENTAGE = "percentage";
    private static final String KEY_EMAIL="email";
    private static final String TABLE_COMPANY = "company";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_CITY = "city";
    private static final String KEY_C_ID = "cid";
    private static final String KEY_PHONE_SEC = "phonesec";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME + " TEXT, "
                + KEY_EMAIL + " TEXT UNIQUE, "+
                 KEY_PHONE + " TEXT UNIQUE, "+
                KEY_PERCENTAGE + " TEXT, "+
                KEY_COURSE + " TEXT, "+
                KEY_FIELD + " TEXT"
                + ")";
        String CREATE_LOGIN_TABLE_COMPANY = "CREATE TABLE " + TABLE_COMPANY + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_C_ID + " INTEGER,"
                + KEY_EMAIL + " TEXT UNIQUE,"+
                KEY_ADDRESS + " TEXT,"+
                KEY_CITY + " TEXT,"+
                KEY_PHONE + " TEXT,"+
                KEY_PHONE_SEC + " TEXT"
                + ")";
        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_LOGIN_TABLE_COMPANY);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPANY);
        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String name, String phone, String course, String field, String percentage,String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_PHONE, phone);
        values.put(KEY_COURSE, course); // Name
        values.put(KEY_FIELD, field);
        values.put(KEY_PERCENTAGE,percentage);// Email
        values.put(KEY_EMAIL,email);

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    public void addCompany(String name, String CID, String email, String phone, String phonesec, String address, String city) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_C_ID, CID);
        values.put(KEY_EMAIL, email);
        values.put(KEY_ADDRESS, address); // Name
        values.put(KEY_CITY, city); // Name
        values.put(KEY_PHONE, phone);
        values.put(KEY_PHONE_SEC, phonesec);// Email
        // Inserting Row
        long id = db.insert(TABLE_COMPANY, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }
    public HashMap<String, String> getCompanyDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_COMPANY;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("cid", cursor.getString(2));
            user.put("email", cursor.getString(3));
            user.put("address", cursor.getString(4));
            user.put("city", cursor.getString(5));
            user.put("phone", cursor.getString(6));
            user.put("phonesec", cursor.getString(7));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("phone",cursor.getString(3));
            user.put("percentage", cursor.getString(4));
            user.put("course", cursor.getString(5));
            user.put("field", cursor.getString(6));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();


        Log.d(TAG, "Deleted all user info from sqlite");
    }

    public void deleteCompany() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_COMPANY, null, null);
        db.close();
        Log.d(TAG, "Deleted all user info from sqlite");
    }

}