package com.ywillems.marsxplorer;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by ywillems on 14-3-2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mars_xplorer";
    private static final int DATABASE_VERSION = 4;
    private final String TAG = "DatabaseHelper";

    public static final String TABLE_NAME = "photos";
    public static final String ID_COLUMN = "_id";
    public static final String CAMERA_COLUMN = "camera";
    public static final String URL_COLUMN = "url";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Creates the SQLite database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                ID_COLUMN +" TEXT PRIMARY KEY, " +
                CAMERA_COLUMN + " TEXT NOT NULL," +
                URL_COLUMN + " TEXT NOT NULL);";
        sqLiteDatabase.execSQL(query);
        Log.i(TAG, "SQLite database created!");
    }

    //Drops the photos table on new version
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS photos");
        onCreate(sqLiteDatabase);
        Log.i(TAG, "Updated database");
    }
}
