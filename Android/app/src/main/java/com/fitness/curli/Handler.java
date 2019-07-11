package com.fitness.curli;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class Handler extends SQLiteOpenHelper {
    //information of database
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ExerciseDb.db";
    public static final String TABLE_NAME = "";
    public static final String COLUMN_ID = "ExerciseID";
    public static final String COLUMN_NAME = "ExerciseName";
    //initialize the database
    public Handler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
    /*
    public void loadHandler() {}
    public void addHandler(ExerciseDb exercise) {}
    public ExerciseDb findHandler(String ExerciseName) {}
    public boolean deleteHandler(int ID) {}
    public boolean updateHandler(int ID, String name) {}
}
    */


}