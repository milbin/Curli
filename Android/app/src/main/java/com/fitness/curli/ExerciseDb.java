package com.fitness.curli;

import java.lang.reflect.Array;
import java.lang.String;
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
    private String Equipment;
    private String ExerciseType;

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

        c=db.rawQuery("Select MainGroup From ExerciseTable Where Name = '"+name+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while(c.moveToNext()){
            GroupStr = c.getString(0);
            buffer.append(""+GroupStr);
        }
        return buffer.toString();
    }
    public String[] getMuscles(String name){
        c = db.rawQuery("Select DetailedGroup From ExerciseTable Where Name = '"+name+"'", new String[]{});


        StringBuffer buffer = new StringBuffer();
        while(c.moveToNext()){
            PrimaryMuscles = c.getString(0);
            buffer.append(""+PrimaryMuscles);
        }

        //String Muscles[] = (buffer.toString()).split(","); //create first part of the list from the first column

        c = db.rawQuery("Select SecondaryGroup From ExerciseTable Where Name = '"+name+"'", new String[]{});


        StringBuffer buffer2 = new StringBuffer();
        while(c.moveToNext()){
            PrimaryMuscles = c.getString(0);
            buffer2.append(""+PrimaryMuscles);
        }

        String[] Muscles = new String[(buffer.toString()).split(",").length + (buffer2.toString()).split(",").length];
        System.arraycopy((buffer.toString()).split(","), 0, Muscles, 0, (buffer.toString()).split(",").length);
        System.arraycopy((buffer2.toString()).split(","), 0, Muscles, (buffer.toString()).split(",").length, (buffer2.toString()).split(",").length);

        return Muscles;

    }

    public String getEquipment(String name){
        c = db.rawQuery("Select Equipment From ExerciseTable Where Name = '"+name+"'", new String[]{});

        StringBuffer buffer = new StringBuffer();
        while(c.moveToNext()){
            Equipment = c.getString(0);
            buffer.append(""+Equipment);
        }
        return buffer.toString();
    }

    public String getExerciseType(String name){
        c = db.rawQuery("Select TypeOfExercise From ExerciseTable Where Name = '"+name+"'", new String[]{});

        StringBuffer buffer = new StringBuffer();
        while(c.moveToNext()){
            ExerciseType = c.getString(0);
            buffer.append(""+ExerciseType);
        }
        return buffer.toString();
    }

}