package com.fitness.curli;

import java.util.ArrayList;
import java.util.Arrays;
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
}
