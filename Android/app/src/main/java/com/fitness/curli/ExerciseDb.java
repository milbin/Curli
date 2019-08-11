package com.fitness.curli;

import java.lang.reflect.Array;
import java.lang.String;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.nio.charset.Charset;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

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
    public ExerciseDb(Context context){
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

    public ArrayList<String> getGroups(){
        c=db.rawQuery("Select MainGroup From ExerciseTable", new String[]{});
        ArrayList<String> groups = new ArrayList();
        while(c.moveToNext()){
            String current = c.getString(0);
            if (!groups.contains(current)) {
                groups.add(current);
            }
        }

        return groups;
    }

    public ArrayList<String> getEquipment(){
        c=db.rawQuery("Select Equipment From ExerciseTable", new String[]{});

        ArrayList<String> equipment = new ArrayList<>();
        while (c.moveToNext()){
            String current = c.getString(0);
            if (!equipment.contains(current)){
                equipment.add(current);
            }
        }

        return equipment;
    }

    public HashMap<String, ArrayList<LinkedHashMap<String,String>>> getExercisesAlphabetized(){
        c=db.rawQuery("Select Name From ExerciseTable", new String[]{});

        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        HashMap<String, ArrayList<LinkedHashMap<String,String>>> exercises = new HashMap();
        for (int index = 0; index < alphabet.length(); index++){
            exercises.put(""+alphabet.charAt(index), new ArrayList<LinkedHashMap<String, String>>());
        }

        if (c.getCount() >= 1 && c.moveToFirst()) {
            while (c.moveToNext()) {
                LinkedHashMap<String, String> currentExercise = new LinkedHashMap<>();
                String name = c.getString(0);
                currentExercise.put("name", name);

                Cursor cursor2 = db.rawQuery("Select Equipment From ExerciseTable Where Name = '" + name + "'", new String[]{});
                cursor2.moveToFirst();
                String equipment = cursor2.getString(0);
                //String equipment = c.getString(c.getColumnIndex("Equipment"));
                currentExercise.put("equipment", equipment);
                cursor2.close();

                cursor2 = db.rawQuery("Select MainGroup From ExerciseTable Where Name = '" + name + "'", new String[]{});
                cursor2.moveToFirst();
                String primaryMuscleGroup = cursor2.getString(0);
                currentExercise.put("primary muscle group", primaryMuscleGroup);
                cursor2.close();

                cursor2 = db.rawQuery("Select SecondaryGroup From ExerciseTable Where Name = '" + name + "'", new String[]{});
                cursor2.moveToFirst();
                String secondaryMuscleGroup = cursor2.getString(0);
                currentExercise.put("secondary muscle group", secondaryMuscleGroup);
                cursor2.close();

                exercises.get("" + name.charAt(0)).add(currentExercise);
            }
        }

        return exercises;
    }

    public HashMap<String, ArrayList<LinkedHashMap<String,String>>> getExercisesAlphabetized(String filterType, String filter){
        c=db.rawQuery("Select Name From ExerciseTable Where "+filterType+" = '"+filter+"'", new String[]{});

        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        HashMap<String, ArrayList<LinkedHashMap<String,String>>> exercises = new HashMap();
        for (int index = 0; index < alphabet.length(); index++){
            exercises.put(""+alphabet.charAt(index), new ArrayList<LinkedHashMap<String, String>>());
        }

        if (c.getCount() >= 1 && c.moveToFirst()) {
            while (c.moveToNext()) {
                LinkedHashMap<String, String> currentExercise = new LinkedHashMap<>();
                String name = c.getString(0);
                currentExercise.put("name", name);

                Cursor cursor2 = db.rawQuery("Select Equipment From ExerciseTable Where Name = '" + name + "'", new String[]{});
                cursor2.moveToFirst();
                String equipment = cursor2.getString(0);
                //String equipment = c.getString(c.getColumnIndex("Equipment"));
                currentExercise.put("equipment", equipment);
                cursor2.close();

                cursor2 = db.rawQuery("Select MainGroup From ExerciseTable Where Name = '" + name + "'", new String[]{});
                cursor2.moveToFirst();
                String primaryMuscleGroup = cursor2.getString(0);
                currentExercise.put("primary muscle group", primaryMuscleGroup);
                cursor2.close();

                cursor2 = db.rawQuery("Select SecondaryGroup From ExerciseTable Where Name = '" + name + "'", new String[]{});
                cursor2.moveToFirst();
                String secondaryMuscleGroup = cursor2.getString(0);
                currentExercise.put("secondary muscle group", secondaryMuscleGroup);
                cursor2.close();

                exercises.get("" + name.charAt(0)).add(currentExercise);
            }
        }

        return exercises;
    }

    public ArrayList<String> getExercises(){
        c=db.rawQuery("SELECT Name FROM ExerciseTable", new String[]{});
        ArrayList<String> exercises = new ArrayList<>();
        while (c.moveToNext()){
            exercises.add(c.getString(0));
        }

        return exercises;
    }
    public ArrayList<String> getExercises(String filterType, String filter){
        c=db.rawQuery("SELECT Name FROM ExerciseTable Where "+filterType+" = '"+filter+"'", new String[]{});
        ArrayList<String> exercises = new ArrayList<>();
        while (c.moveToNext()){
            exercises.add(c.getString(0));
        }

        return exercises;
    }

    public String getGroup(String name) {

        c=db.rawQuery("Select MainGroup From ExerciseTable Where Name = '"+name+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while(c.moveToNext()){
            GroupStr = c.getString(0);
            buffer.append(""+GroupStr);
        }

        c.close();
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