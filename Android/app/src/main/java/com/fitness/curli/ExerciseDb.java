package com.fitness.curli;

public class ExerciseDb {

    //fields
    private String ExerciseName;
    private String Group;
    private String[] PrimaryMuscles;

    //constructors
    ExerciseDb() {}
    ExerciseDb(String name, String group, String[] muscles){
        this.ExerciseName = name;
        this.Group = group;
        this.PrimaryMuscles = muscles;
    }

    //properties
    public String getName(){
        return ExerciseName;
    }
    public String getGroup() {
        return Group;
    }
    public String[] getMuscles(){
        return PrimaryMuscles;
    }

}
