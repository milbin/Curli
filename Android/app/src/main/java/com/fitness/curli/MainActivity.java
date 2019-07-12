package com.fitness.curli;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RelativeLayout workoutPlaceholder = findViewById(R.id.workout_card);
        workoutPlaceholder.setOnClickListener(new onWorkoutClick());



    }


    public class onWorkoutClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            LinkedHashMap BP = new LinkedHashMap<>();
            BP.put("title", "Bench Press");
            BP.put("weight", 135);
            BP.put("reps", 8);

            LinkedHashMap BC = new LinkedHashMap<>();
            BC.put("title", "Bicep Curl");
            BC.put("weight", 75);
            BC.put("reps", 8);

            ArrayList BCList = new ArrayList();
            ArrayList BPList = new ArrayList();

            for (int i = 0; i < 3; i++) {
                BCList.add(BC);
                BPList.add(BP);
            }

            ArrayList exercises = new ArrayList();
            exercises.add(BPList);
            exercises.add(BCList);
            LinkedHashMap workout = new LinkedHashMap();
            workout.put("title", "Arms and Chest");
            workout.put("exercises", exercises);

            System.out.println(workout);



            Intent myIntent = new Intent(MainActivity.this, WorkoutActivity.class);
            myIntent.putExtra("workout", workout);
            startActivity(myIntent);
        }
    }
}
