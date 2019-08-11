package com.fitness.curli;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class SQLData {
    private HashMap<String, ArrayList<LinkedHashMap<String,String>>> exercises = new HashMap<String, ArrayList<LinkedHashMap<String,String>>>();

    public HashMap<String, ArrayList<String>> MUSCLE_TO_EXCERCISE = new LinkedHashMap<>();
    public HashMap<String, ArrayList<String>> GROUP_TO_MUSCLE = new LinkedHashMap<>();
    public ArrayList<String> GROUPS = new ArrayList<>(Arrays.asList("legs", "arms", "core"));
    public ArrayList<String> MUSCLES = new ArrayList<>(Arrays.asList("biceps", "triceps", "quads", "hams", "abs", "lats"));
    public ArrayList<String> EXERCISES = new ArrayList<>(Arrays.asList("dumbbel curl", "barbell curl", "skull crusher", "rope extension","leg extension", "squat", "leg curl", "deadlift", "crunches", "sit ups", "pull up"));

    /*INFO VIEW DATA STRUCTURE: The methods within this class should parse the raw sql sqlData into the sqlData structure outlined in the example below

    {"A":{{"name":"Ab Crunch (Machine)", "equipment":"Machine", "primary muscle group":"abs", "secondary muscle group":"obliques", "instructions":"placeholder"}},
    "B":{{"name":"Bicep Curl (Dumbbell)", "equipment":"Dumbbell", "primary muscle group":"biceps", "secondary muscle group":"NA", "instructions":"placeholder"}}}
     */

    public SQLData(){


        MUSCLE_TO_EXCERCISE.put("biceps",  new ArrayList<String>(Arrays.asList("dumbbell curl", "barbell curl")));
        MUSCLE_TO_EXCERCISE.put("triceps", new ArrayList<String>(Arrays.asList("skull crushers", "rope extension")));
        MUSCLE_TO_EXCERCISE.put("quads",   new ArrayList<String>(Arrays.asList("leg extension", "squat")));
        MUSCLE_TO_EXCERCISE.put("hams",    new ArrayList<String>(Arrays.asList("leg curl", "deadlift")));
        MUSCLE_TO_EXCERCISE.put("abs",     new ArrayList<String>(Arrays.asList("crunches", "sit ups")));
        MUSCLE_TO_EXCERCISE.put("lats",    new ArrayList<String>(Arrays.asList("pull up")));

        GROUP_TO_MUSCLE.put("arms", new ArrayList<String>(Arrays.asList("biceps", "triceps")));
        GROUP_TO_MUSCLE.put("legs", new ArrayList<String>(Arrays.asList("quads", "hams")));
        GROUP_TO_MUSCLE.put("core", new ArrayList<String>(Arrays.asList("abs", "lats")));
    }

    public HashMap<String, ArrayList<LinkedHashMap<String,String>>> getExercises(){
        //TODO add actual sql parsing from the database that jacks working on


        LinkedHashMap<String,String> exercise = new LinkedHashMap<>();
        exercise.put("name", "Ab Crunch");
        exercise.put("equipment", "Machine");
        exercise.put("primary muscle group", "abs");
        exercise.put("secondary muscle group", "obliques");

        LinkedHashMap<String,String> exercise1 = new LinkedHashMap<>();
        exercise1.put("name", "Ab Extensions");
        exercise1.put("equipment", "Machine");
        exercise1.put("primary muscle group", "abs");
        exercise1.put("secondary muscle group", "obliques");

        LinkedHashMap<String,String> exercise2 = new LinkedHashMap<>();
        exercise2.put("name", "Assisted Deadlift");
        exercise2.put("equipment", "Barbell");
        exercise2.put("primary muscle group", "back");
        exercise2.put("secondary muscle group", "glutes");

        LinkedHashMap<String,String> exercise3 = new LinkedHashMap<>();
        exercise3.put("name", "Ab Placeholder");
        exercise3.put("equipment", "Bands");
        exercise3.put("primary muscle group", "abs");
        exercise3.put("secondary muscle group", "obliques");

        ArrayList exerciseList = new ArrayList<>();
        exerciseList.add(exercise);
        exerciseList.add(exercise1);
        exerciseList.add(exercise2);
        exerciseList.add(exercise3);

        ArrayList exerciseList1 = new ArrayList<>();
        exerciseList1.add(exercise);
        exerciseList1.add(exercise1);
        exerciseList1.add(exercise2);
        exerciseList1.add(exercise3);
        exerciseList1.add(exercise);
        exercises.put("A", exerciseList);
        exercises.put("B", exerciseList1);
        exercises.put("C", exerciseList);
        return exercises;
    }





    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    Cursor c = null;
    public void openWorkoutHistoryDB(Context context){
        this.openHelper = new DatabaseOpenHelper(context, 1, "WorkoutHistoryDB.sqlite");
        this.db = openHelper.getWritableDatabase();
    }
    public void openExerciseDB(Context context){
        this.openHelper = new DatabaseOpenHelper(context, 1, "ExerciseDB");
        this.db = openHelper.getWritableDatabase();
    }

    public void saveWorkout(HashMap json) {
        int id = 1;
        Date calendar = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String date = df.format(calendar);
        String title = (String) json.get("title");
        Gson gson = new Gson();
        String jsonString = gson.toJson(json);

        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("date", date);
        values.put("title", title);
        values.put("json", jsonString);

        db.insert("WorkoutHistory", null, values);
        c = db.rawQuery("SELECT * FROM WorkoutHistory;", new String[]{});
        c.moveToFirst();
        db.close();
    }

    public String getGroupFromName(String name) {

        c=db.rawQuery("Select MainGroup From ExerciseTable Where Name = '"+name+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while(c.moveToNext()){
            String GroupStr = c.getString(0);
            buffer.append(""+GroupStr);
        }
        db.close();
        return buffer.toString();
    }
    public String[] getMusclesFromName(String name){
        c = db.rawQuery("Select DetailedGroup From ExerciseTable Where Name = '"+name+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while(c.moveToNext()){
            String PrimaryMuscles = c.getString(0);
            buffer.append(""+PrimaryMuscles);
        }
         //create first part of the list from the first column
        c = db.rawQuery("Select SecondaryGroup From ExerciseTable Where Name = '"+name+"'", new String[]{});

        StringBuffer buffer2 = new StringBuffer();
        while(c.moveToNext()){
            String PrimaryMuscles = c.getString(0);
            buffer2.append(""+PrimaryMuscles);
        }

        String[] Muscles = new String[(buffer.toString()).split(",").length + (buffer2.toString()).split(",").length];
        System.arraycopy((buffer.toString()).split(","), 0, Muscles, 0, (buffer.toString()).split(",").length);
        System.arraycopy((buffer2.toString()).split(","), 0, Muscles, (buffer.toString()).split(",").length, (buffer2.toString()).split(",").length);
        db.close();
        return Muscles;

    }

    public String getEquipmentFromName(String name){
        c = db.rawQuery("Select Equipment From ExerciseTable Where Name = '"+name+"'", new String[]{});

        StringBuffer buffer = new StringBuffer();
        while(c.moveToNext()){
            String Equipment = c.getString(0);
            buffer.append(""+Equipment);
        }
        db.close();
        return buffer.toString();
    }

    public String getExerciseTypeFromName(String name){
        c = db.rawQuery("Select TypeOfExercise From ExerciseTable Where Name = '"+name+"'", new String[]{});

        StringBuffer buffer = new StringBuffer();
        while(c.moveToNext()){
            String ExerciseType = c.getString(0);
            buffer.append(""+ExerciseType);
        }
        db.close();
        return buffer.toString();
    }
}
