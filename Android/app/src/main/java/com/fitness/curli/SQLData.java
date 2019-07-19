package com.fitness.curli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class SQLData {
    public HashMap<String, ArrayList<String>> MUSCLE_TO_EXCERCISE = new LinkedHashMap<>();
    public HashMap<String, ArrayList<String>> GROUP_TO_MUSCLE = new LinkedHashMap<>();

    public SQLData(){
        MUSCLE_TO_EXCERCISE.put("biceps",  new ArrayList<String>(Arrays.asList("dumbbell curl", "barbell curl")));
        MUSCLE_TO_EXCERCISE.put("triceps", new ArrayList<String>(Arrays.asList("skull crushers", "rope extension")));
        MUSCLE_TO_EXCERCISE.put("quads",   new ArrayList<String>(Arrays.asList("leg extension", "squat")));
        MUSCLE_TO_EXCERCISE.put("hams",    new ArrayList<String>(Arrays.asList("leg curl", "deadlift")));
        MUSCLE_TO_EXCERCISE.put("abs",     new ArrayList<String>(Arrays.asList("crunches", "sit ups")));
        MUSCLE_TO_EXCERCISE.put("lats",    new ArrayList<String>(Arrays.asList("pull up", "")));

        GROUP_TO_MUSCLE.put("arms", new ArrayList<String>(Arrays.asList("biceps", "triceps")));
        GROUP_TO_MUSCLE.put("legs", new ArrayList<String>(Arrays.asList("quads", "hams")));
        GROUP_TO_MUSCLE.put("core", new ArrayList<String>(Arrays.asList("abs", "lats")));
    }
}
