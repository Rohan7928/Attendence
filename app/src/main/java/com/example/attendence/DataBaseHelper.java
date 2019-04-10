package com.example.attendence;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "Rohan";
    public static int DATABASE_VERSION = 1;
    public String TABLE_NAME = "Students";
    public String COLUMN_LOGIN = "login";
    public String COLUMN_PASSWORD = "password";
    public String CREATE_TABLE = "create table Student(user text,password text);";

    @Override
    public SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    long save(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_LOGIN,username);
        contentValues.put(COLUMN_PASSWORD,password);
        long id=db.insert(TABLE_NAME,null,contentValues);
        db.close();
        return id;

    }
}