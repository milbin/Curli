package com.fitness.curli;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import java.util.HashMap;

public class Curli extends Application {
    private WorkoutTimer timer;
    private HashMap workout;
    @Override
    public void onCreate() {
        super.onCreate();
    }
    public void setWorkoutTimer(WorkoutTimer t){
        timer = t;
    }
    public WorkoutTimer getWorkoutTimer(){
        return timer;
    }
    public HashMap getWorkout() {
        return workout;
    }
    public void setWorkout(HashMap workoutObject) {
        this.workout = workoutObject;
    }
}
