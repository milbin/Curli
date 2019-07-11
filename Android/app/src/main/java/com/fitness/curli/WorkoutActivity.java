package com.fitness.curli;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WorkoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_activity);
        LinearLayout linearLayout = findViewById(R.id.linearLayoutWorkout);

        View relativeLayout = LayoutInflater.from(this).inflate(R.layout.exercise_card, null);
        linearLayout.addView(relativeLayout);
        TextView exerciseName = relativeLayout.findViewById(R.id.exercise_name);
        exerciseName.setText("Leg Press");

        View relativeLayout1 = LayoutInflater.from(this).inflate(R.layout.exercise_card, null);
        linearLayout.addView(relativeLayout1);
        TextView exerciseName1 = relativeLayout1.findViewById(R.id.exercise_name);
        exerciseName1.setText("Leg Press");


    }
}