package com.fitness.curli;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout linearLayout = findViewById(R.id.linear_layout);

        View relativeLayout = LayoutInflater.from(this).inflate(R.layout.workout_card, null);
        linearLayout.addView(relativeLayout);
        TextView exerciseName = relativeLayout.findViewById(R.id.exercise_name);
        exerciseName.setText("Leg Press");

        View relativeLayout1 = LayoutInflater.from(this).inflate(R.layout.workout_card, null);
        linearLayout.addView(relativeLayout1);
        TextView exerciseName1 = relativeLayout1.findViewById(R.id.exercise_name);
        exerciseName1.setText("Bicep Curl");

        View relativeLayout2 = LayoutInflater.from(this).inflate(R.layout.workout_card, null);
        linearLayout.addView(relativeLayout2);
        TextView exerciseName2 = relativeLayout2.findViewById(R.id.exercise_name);
        exerciseName2.setText("Tricep Curl");
    }
}
