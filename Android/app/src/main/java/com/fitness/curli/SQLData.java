package com.fitness.curli;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class SQLData {
    /*INFO VIEW DATA STRUCTURE: The methods within this class should parse the raw sql sqlData into the sqlData structure outlined in the example below

    {"A":{{"name":"Ab Crunch (Machine)", "equipment":"Machine", "primary muscle group":"abs", "secondary muscle group":"obliques", "instructions":"placeholder"}},
    "B":{{"name":"Bicep Curl (Dumbbell)", "equipment":"Dumbbell", "primary muscle group":"biceps", "secondary muscle group":"NA", "instructions":"placeholder"}}}
     */

    public ArrayList<String> getGroups(){
        c=db.rawQuery("Select Group1 From ExerciseTable", new String[]{});
        ArrayList<String> groups = new ArrayList();
        while(c.moveToNext()){
            String current = c.getString(0);
            if (!groups.contains(current)) {
                groups.add(current);
            }
        }

        return groups;
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

                Cursor cursor2 = db.rawQuery("Select Equipment1 From ExerciseTable Where Name = '" + name + "'", new String[]{});
                cursor2.moveToFirst();
                String equipment = cursor2.getString(0);
                //String equipment = c.getString(c.getColumnIndex("Equipment"));
                currentExercise.put("equipment", equipment);
                cursor2.close();

                cursor2 = db.rawQuery("Select Group1 From ExerciseTable Where Name = '" + name + "'", new String[]{});
                cursor2.moveToFirst();
                String primaryMuscleGroup = cursor2.getString(0);
                currentExercise.put("primary muscle group", primaryMuscleGroup);
                cursor2.close();

                cursor2 = db.rawQuery("Select Group2 From ExerciseTable Where Name = '" + name + "'", new String[]{});
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





    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    Cursor c = null;
    public void openUserDB(Context context){
        this.openHelper = new DatabaseOpenHelper(context, 1, "UserDB.sqlite");
        this.db = openHelper.getWritableDatabase();
    }
    public void openExerciseDB(Context context){
        this.openHelper = new DatabaseOpenHelper(context, 1, "ExerciseDB.db");
        this.db = openHelper.getWritableDatabase();
    }

    public void closeDB(){
        db.close();
    }

    public void saveWorkoutToHistory(HashMap json) {
        c = db.rawQuery("SELECT * FROM WorkoutHistory;", new String[]{});
        int id = c.getCount();
        Date calendar = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String date = df.format(calendar);
        String title = (String) json.get("title");
        String time = (String) json.get("time");
        Gson gson = new Gson();
        String jsonString = gson.toJson(json);

        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("date", date);
        values.put("time", time);
        values.put("title", title);
        values.put("json", jsonString);

        db.insert("WorkoutHistory", null, values);
        c = db.rawQuery("SELECT * FROM WorkoutHistory;", new String[]{});
        c.moveToFirst();
    }
    public void saveWorkout(HashMap json) {
        String title = (String) json.get("title");
        Gson gson = new Gson();
        String jsonString = gson.toJson(json);
        c = db.rawQuery("SELECT * FROM Workouts;", new String[]{});

        ContentValues values = new ContentValues();
        values.put("id", c.getCount());
        values.put("name", title);
        values.put("workout", jsonString);


        db.insert("Workouts", null, values);
        c.moveToFirst();
    }
    public void updateWorkout(int id, HashMap json) {
        String title = (String) json.get("title");
        Gson gson = new Gson();
        String jsonString = gson.toJson(json);

        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("name", title);
        values.put("workout", jsonString);

        db.update("Workouts", values, "id="+id, null);
    }

    public void deleteWorkout(int id) {
        db.delete("Workouts", "id=" + id, null);
        c=db.rawQuery("Select * From Workouts;", new String[]{});
        c.moveToFirst();
        for(int i=0;i<=c.getCount();i++){
            Cursor cursor = db.rawQuery("Select id, name, workout From Workouts Where id = "+i, new String[]{});
            if(cursor.getCount() == 0) {
                ContentValues values = new ContentValues();
                values.put("id", i);
                values.put("name", c.getString(1));
                values.put("workout", c.getString(2));
                db.insert("Workouts",  null, values);
            }
            c.moveToNext();
        }


    }

    public int getWorkoutCount(){
        c=db.rawQuery("Select * From Workouts;", new String[]{});
        c.moveToFirst();
        int returnInt = c.getCount();
        return returnInt;
    }

    public ArrayList getworkout(int number) {

        c=db.rawQuery("Select id, name, workout From Workouts Where id = "+number, new String[]{});
        c.moveToFirst();
        System.out.println(number);
        String title = c.getString(1);
        String jsonString = c.getString(2);
        Gson gson = new Gson();
        java.lang.reflect.Type type = new TypeToken<HashMap<String, Object>>(){}.getType();
        HashMap workout = gson.fromJson(jsonString, type);
        ArrayList returnList = new ArrayList();
        returnList.add(title);
        returnList.add(workout);
        return returnList;
    }

    public String getGroup1FromName(String name) {

        c=db.rawQuery("Select Group1 From ExerciseTable Where Name = '"+name+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while(c.moveToNext()){
            String GroupStr = c.getString(0);
            buffer.append(""+GroupStr);
        }
        return buffer.toString();
    }
    public String getGroup2FromName(String name) {

        c=db.rawQuery("Select Group2 From ExerciseTable Where Name = '"+name+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while(c.moveToNext()){
            String GroupStr = c.getString(0);
            buffer.append(""+GroupStr);
        }
        return buffer.toString();
    }
    public String getGroup3FromName(String name) {

        c=db.rawQuery("Select Group3 From ExerciseTable Where Name = '"+name+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while(c.moveToNext()){
            String GroupStr = c.getString(0);
            buffer.append(""+GroupStr);
        }
        return buffer.toString();
    }


    public String getPrimaryEquipmentFromName(String name){
        c = db.rawQuery("Select Equipment1 From ExerciseTable Where Name = '"+name+"'", new String[]{});

        StringBuffer buffer = new StringBuffer();
        while(c.moveToNext()){
            String Equipment = c.getString(0);
            buffer.append(""+Equipment);
        }
        return buffer.toString();
    }
    public String getSecondaryEquipmentFromName(String name){
        c = db.rawQuery("Select Equipment2 From ExerciseTable Where Name = '"+name+"'", new String[]{});

        StringBuffer buffer = new StringBuffer();
        while(c.moveToNext()){
            String Equipment = c.getString(0);
            buffer.append(""+Equipment);
        }
        return buffer.toString();
    }

    public String getExerciseDifficultyFromName(String name){
        c = db.rawQuery("Select Difficulty From ExerciseTable Where Name = '"+name+"'", new String[]{});

        StringBuffer buffer = new StringBuffer();
        while(c.moveToNext()){
            String ExerciseType = c.getString(0);
            buffer.append(""+ExerciseType);
        }
        return buffer.toString();
    }

    public String getExercisePopularityFromName(String name){
        c = db.rawQuery("Select Popularity From ExerciseTable Where Name = '"+name+"'", new String[]{});

        StringBuffer buffer = new StringBuffer();
        while(c.moveToNext()){
            String ExerciseType = c.getString(0);
            buffer.append(""+ExerciseType);
        }
        return buffer.toString();
    }
}
