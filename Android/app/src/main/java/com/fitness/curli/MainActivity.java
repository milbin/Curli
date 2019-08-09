package com.fitness.curli;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.jar.Attributes;

import java.sql.*;

public class MainActivity extends AppCompatActivity {
    //Handler helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RelativeLayout workoutPlaceholder = findViewById(R.id.workout_card);
        workoutPlaceholder.setOnClickListener(new onWorkoutClick());

        System.out.println("HEY");

        ExerciseDb exerciseDb = ExerciseDb.getInstance(getApplicationContext());
        exerciseDb.open();

        System.out.println(exerciseDb.getGroup("Ab Crunch Machine"));

        String[] muscleArray = exerciseDb.getMuscles("Alternate Leg Diagonal Bound");

        for (String element: muscleArray) {
            System.out.println(element);
        }



        /*Handler helper = new Handler(this);

        String name = "Bicep Curl";
        String group = "Arms";
        String muscles = "Bicep";

        ExerciseDb exercise = new ExerciseDb();

        System.out.println(exercise.getTable());

        //helper.insertData(name, group, muscles);*/
    }


    public class onWorkoutClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(MainActivity.this, WorkoutActivity.class);
            startActivity(myIntent);
        }
    }
}
