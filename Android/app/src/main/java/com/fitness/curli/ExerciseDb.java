package com.fitness.curli;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.*;

public class ExerciseDb {

    //fields
    private String ExerciseName;
    private String GroupStr;
    private String PrimaryMuscles;

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static ExerciseDb instance;

    Cursor c = null;

    //private constructor
    private ExerciseDb(Context context){
        this.openHelper = new DatabaseOpenHelper(context);

    }

    //return instance of databases
    public static ExerciseDb getInstance(Context context){
        if(instance == null){
            instance = new ExerciseDb(context);
        }
        return instance;
    }

    //setup open connection to db
    public void open(){
        this.db = openHelper.getWritableDatabase();
    }

    //close the connection
    public void close(){
        if(db == null){
            this.db.close();
        }
    }

    //query and return results from db
    //properties

    public String getName(){
        return ExerciseName;
    }
    public String getGroup(String name) {


        c=db.rawQuery("Select [MainGroup] From Table1 Where Name = '"+name+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while(c.moveToNext()){
            GroupStr = c.getString(0);
            buffer.append(""+GroupStr);
        }
        return buffer.toString();
    }
    public String getMuscles(String name){
        c = db.rawQuery("Select Muscles From Table1 Where Name = '"+name+"'", new String[]{});

        StringBuffer buffer = new StringBuffer();
        while(c.moveToNext()){
            PrimaryMuscles = c.getString(0);
            buffer.append(""+PrimaryMuscles);
        }
        return buffer.toString();
    }

}
