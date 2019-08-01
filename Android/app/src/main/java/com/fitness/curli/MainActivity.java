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

        RelativeLayout workoutPlaceholder1 = findViewById(R.id.workout_card1);
        workoutPlaceholder1.setOnClickListener(new onWorkoutClick1());



    }

    public class onWorkoutClick1 implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, InfoView.class);
            startActivity(intent);
        }
    }


    public class onWorkoutClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            LinkedHashMap BP = new LinkedHashMap<>();
            BP.put("title", "Bench Press");
            BP.put("weight", 135.0);
            BP.put("reps", 8);

            LinkedHashMap BC = new LinkedHashMap<>();
            BC.put("title", "Bicep Curl");
            BC.put("weight", 75.0);
            BC.put("reps", 8);
            LinkedHashMap BC1 = new LinkedHashMap<>();
            BC1.put("title", "Bicep Curl");
            BC1.put("weight", 80.0);
            BC1.put("reps", 9);
            LinkedHashMap BC2 = new LinkedHashMap<>();
            BC2.put("title", "Bicep Curl");
            BC2.put("weight", 90.0);
            BC2.put("reps", 10);

            ArrayList BCList = new ArrayList();
            ArrayList BPList = new ArrayList();

            BCList.add(0);
            BPList.add(0);

            for (int i = 0; i < 3; i++) {
                BPList.add(BP);
            }
            BCList.add(BC);
            BCList.add(BC1);
            BCList.add(BC2);

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
