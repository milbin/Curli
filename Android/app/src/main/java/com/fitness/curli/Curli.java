package com.fitness.curli;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

public class Curli extends Application {
    private WorkoutTimer timer;
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

}
