package com.example.admin1;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ClinicAdminDB";
    private static final String TABLE_NAME = "staff";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "IDENTIFICATION_NUMBER";
    private static final String COL_3 = "PASSWORD";  // Password column

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_2 + " TEXT UNIQUE NOT NULL, " +
                COL_3 + " TEXT NOT NULL)");
        populateInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    private void populateInitialData(SQLiteDatabase db) {
        String[] ids = {"12345", "23456", "34567", "45678", "56789"};
        String commonPassword = "@DutClinic2024";  // Common password for all entries
        for (String idNumber : ids) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_2, idNumber);
            contentValues.put(COL_3, commonPassword);
            db.insert(TABLE_NAME, null, contentValues);
        }
    }

    public boolean checkStaff(String idNumber, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COL_1};
        String selection = COL_2 + " = ? AND " + COL_3 + " = ?";
        String[] selectionArgs = {idNumber, password};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public Cursor getAllAppointments() {
        return null;
    }

    public void deleteAppointment(String id) {
    }
}

