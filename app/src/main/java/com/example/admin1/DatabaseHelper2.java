package com.example.admin1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

public class DatabaseHelper2 extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper2"; // For logging
    private static final String DATABASE_NAME = "AppointmentsDB";
    private static final String TABLE_NAME = "appointments";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "USER_ID";
    private static final String COL_3 = "DATE_TIME";

    public DatabaseHelper2(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_2 + " TEXT, " +
                COL_3 + " TEXT)";
        try {
            db.execSQL(CREATE_TABLE);
        } catch (Exception e) {
            Log.e(TAG, "Error creating database table: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        } catch (Exception e) {
            Log.e(TAG, "Error upgrading database: " + e.getMessage());
        }
    }

    public boolean insertAppointment(String userId, String dateTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, userId);
        contentValues.put(COL_3, dateTime);
        long result = -1;
        try {
            db.beginTransaction();
            result = db.insert(TABLE_NAME, null, contentValues);
            if (result != -1) {
                db.setTransactionSuccessful();
                Log.d(TAG, "Insert successful: User ID - " + userId + ", DateTime - " + dateTime);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error inserting data: " + e.getMessage());
        } finally {
            db.endTransaction();
            db.close();
        }
        return result != -1;
    }


    public Cursor getAllAppointments() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        } catch (Exception e) {
            Log.e(TAG, "Error reading all appointments: " + e.getMessage());
        }
        return cursor;
    }

    public boolean deleteAppointment(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = 0;
        try {
            result = db.delete(TABLE_NAME, "ID = ?", new String[]{id});
        } catch (Exception e) {
            Log.e(TAG, "Error deleting appointment: " + e.getMessage());
        } finally {
            db.close();
        }
        return result > 0;
    }
}

