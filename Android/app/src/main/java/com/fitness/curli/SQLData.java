package com.fitness.curli;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
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

    public void createTablesNew(){
        //contains basic profile information about the user
        db.execSQL("DROP TABLE IF EXISTS user_info");
        db.execSQL("CREATE TABLE user_info (user_guide INTEGER, token TEXT, first_name TEXT, last_name TEXT, height FLOAT, birth_date TEXT, registration_date TEXT)");

        //contains the workout a user has created. links back to user
        db.execSQL("DROP TABLE IF EXISTS workout");
        db.execSQL("CREATE TABLE workout (workout_guide INTEGER, exercise_guide INTEGER, user_guide INTEGER, workout_name TEXT)");

        //contains each exercise in a given workout, links back to related workout and user
        db.execSQL("DROP TABLE IF EXISTS exercise");
        db.execSQL("CREATE TABLE exercise (exercise_guide INTEGER, workout_guide INTEGER, user_guide INTEGER, exercise_sets_guide INTEGER, one_rep_max FLOAT)");

        //contains specific information about individual sets for each exercise. links back to related exercise, workout, and user
        db.execSQL("DROP TABLE IF EXISTS exercise_sets");
        db.execSQL("CREATE TABLE exercise_sets (exercise_sets_guide INTEGER, exercise_guide INTEGER, workout_guide INTEGER, user_guide INTEGER, set_number INTEGER, exercise_name TEXT, reps_number INTEGER, weight FLOAT)");

        //contains information about past workout the user has completed, links back to related workout
        db.execSQL("DROP TABLE IF EXISTS workout_history");
        db.execSQL("CREATE TABLE workout_history (workout_history_guide INTEGER, workout_guide INTEGER, user_guide INTEGER, order_number INTEGER, date DATE, time_taken TIME)");

        //contains information about past exercises the user has completed, links back to related completed workout, user
        db.execSQL("DROP TABLE IF EXISTS exercise_history");
        db.execSQL("CREATE TABLE exercise_history (exercise_history_guide INTEGER, set_history_guide INTEGER, workout_history_guide INTEGER, exercise_guide INTEGER, workout_guide INTEGER, user_guide)");

        //contains information about past completed sets, links back to related completed exercise, completed workout, user
        db.execSQL("DROP TABLE IF EXISTS set_history");
        db.execSQL("CREATE TABLE set_history (set_history_guide INTEGER, guide_user INTEGER, set_number INTEGER, reps INTEGER, weight FLOAT)");

        //contains information about user weight over time, links back to user
        db.execSQL("DROP TABLE IF EXISTS weight_record");
        db.execSQL("CREATE TABLE weight_record (user_guide INTEGER, weight FLOAT, date DATE)");

        //contains information about the user muscle measurement over time, links back to user
        db.execSQL("DROP TABLE IF EXISTS muscle_size_record");
        db.execSQL("CREATE TABLE muscle_size_record (user_guide INTEGER, arms_size FLOAT, quads_size FLOAT, chest_size FLOAT, waist_size FLOAT, date DATE)");
    }

    public void createUser(String token, String firstName, String lastName, Float height, Date birthDate){
        int user_guide = 0;
        try {
            c = db.rawQuery("SELECT MAX(user_guide) FROM user_info", new String[]{});
            String id = c.getString(0);
            user_guide = Integer.valueOf(id) + 1;
        }
        catch (CursorIndexOutOfBoundsException e){
            System.out.println(e);
        }

        Date calendar = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String registrationDate = df.format(calendar);

        String birthDateString = new SimpleDateFormat("dd-MM-yyyy").format(birthDate);

        ContentValues values = new ContentValues();
        values.put("user_guide", user_guide);
        values.put("token", token);
        values.put("first_name", firstName);
        values.put("last_name", lastName);
        values.put("height", height);
        values.put("birth_date", birthDateString);
        values.put("registration_date", registrationDate);

        db.insert("user_info", null, values);
    }

    public void saveWorkoutToHistory(HashMap json) {
        int id = 1;
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

        System.out.println("JSON = " + jsonString);

        db.insert("Workouts", null, values);
        c.moveToFirst();
    }

    public void saveWorkoutNew(HashMap json){
        int user_guide = 0;
        int workout_guide = 0;
        int exercise_guide = 0;
        int set_guide = 0;
        try {
            c = db.rawQuery("SELECT MAX(workout_guide) FROM workout", new String[]{});
            workout_guide = Integer.valueOf(c.getString(0));

            c = db.rawQuery("SELECT MAX(exercise_guide) FROM exercise", new String[]{});
            exercise_guide = Integer.valueOf(c.getString(0)) + 1;
            c = db.rawQuery("SELECT MAX(exercise_sets_guide) FROM exercise_sets", new String[]{});
            set_guide = Integer.valueOf(c.getString(0)) + 1;
        }
        catch (CursorIndexOutOfBoundsException e){
            System.out.println(e);
        }
        System.out.println(user_guide);
        System.out.println(json);

        String workoutTitle = (String) json.get("title");
        ArrayList<ArrayList> exercises = (ArrayList) json.get("exercises");
        for (ArrayList exercise : exercises){
            ContentValues exerciseValues = new ContentValues();
            exerciseValues.put("exercise_guide", exercise_guide);
            exerciseValues.put("workout_guide", workout_guide);
            exerciseValues.put("user_guide", user_guide);
            exerciseValues.put("exercise_sets_guide", set_guide);
            exerciseValues.put("one_rep_max", 0);
            db.insert("exercise", null, exerciseValues);
            for (int setIndex = 0; setIndex < exercises.size()/2; setIndex += 2){
                ContentValues setValues = new ContentValues();

                int setNumber = (int)(exercise.get(setIndex));
                HashMap setInfo = (HashMap) (exercise.get(setIndex+1));
                String setTitle = (String) (setInfo.get("title"));
                Float weight = (Float) (setInfo.get("weight"));
                int reps = (int) (setInfo.get("reps"));

                setValues.put("exercise_sets_guide", set_guide);
                setValues.put("exercise_guide", exercise_guide);
                setValues.put("workout_guide", workout_guide);
                setValues.put("user_guide", user_guide);
                setValues.put("set_number", setNumber);
                setValues.put("exercise_name", setTitle);
                setValues.put("reps_number", reps);
                setValues.put("weight", weight);

                db.insert("exercise_sets", null, setValues);
            }
            set_guide ++;
        }
        ContentValues workoutValues = new ContentValues();
        workoutValues.put("workout_guide", workout_guide);
        workoutValues.put("exercise_guide", exercise_guide);
        workoutValues.put("user_guide", user_guide);
        workoutValues.put("workout_name", workoutTitle);

        db.insert("workout", null, workoutValues);

        System.out.println("DONE");
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

    public boolean deleteWorkout(int id) {
        return db.delete("Workouts", "id" + "=" + id, null) > 0;
    }

    public int getWorkoutCount(){
        c=db.rawQuery("Select * From Workouts;", new String[]{});
        int returnInt = c.getCount();
        return returnInt;
    }

    public ArrayList getworkout(int number) {

        c=db.rawQuery("Select id, name, workout From Workouts Where id = "+number, new String[]{});
        c.moveToFirst();
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

    public String getGroupFromName(String name) {

        c=db.rawQuery("Select MainGroup From ExerciseTable Where Name = '"+name+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while(c.moveToNext()){
            String GroupStr = c.getString(0);
            buffer.append(""+GroupStr);
        }
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
        return Muscles;

    }

    public String getEquipmentFromName(String name){
        c = db.rawQuery("Select Equipment From ExerciseTable Where Name = '"+name+"'", new String[]{});

        StringBuffer buffer = new StringBuffer();
        while(c.moveToNext()){
            String Equipment = c.getString(0);
            buffer.append(""+Equipment);
        }
        return buffer.toString();
    }

    public String getExerciseTypeFromName(String name){
        c = db.rawQuery("Select TypeOfExercise From ExerciseTable Where Name = '"+name+"'", new String[]{});

        StringBuffer buffer = new StringBuffer();
        while(c.moveToNext()){
            String ExerciseType = c.getString(0);
            buffer.append(""+ExerciseType);
        }
        return buffer.toString();
    }
}
