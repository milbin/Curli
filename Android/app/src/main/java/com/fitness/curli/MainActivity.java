package com.fitness.curli;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity {
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RelativeLayout workoutPlaceholder = findViewById(R.id.workout_card);
        workoutPlaceholder.setOnClickListener(new onWorkoutClick());

        //set on click listener for the bottom bar buttons
        findViewById(R.id.history).setOnClickListener(new onNavbarClick());
        findViewById(R.id.schedule).setOnClickListener(new onNavbarClick());
        findViewById(R.id.workout).setOnClickListener(new onNavbarClick());
        findViewById(R.id.exercises).setOnClickListener(new onNavbarClick());
        findViewById(R.id.progress).setOnClickListener(new onNavbarClick());



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

    public class onNavbarClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //grey out all other icons and textviews
            LinearLayout linearLayout = (LinearLayout)v.getParent().getParent();
            ((ImageView)findViewById(R.id.history)).setColorFilter(Color.parseColor("#5f6267"), android.graphics.PorterDuff.Mode.SRC_IN);
            ((ImageView)findViewById(R.id.schedule)).setColorFilter(Color.parseColor("#5f6267"), android.graphics.PorterDuff.Mode.SRC_IN);
            ((ImageView)findViewById(R.id.workout)).setColorFilter(Color.parseColor("#5f6267"), android.graphics.PorterDuff.Mode.SRC_IN);
            ((ImageView)findViewById(R.id.exercises)).setColorFilter(Color.parseColor("#5f6267"), android.graphics.PorterDuff.Mode.SRC_IN);
            ((ImageView)findViewById(R.id.progress)).setColorFilter(Color.parseColor("#5f6267"), android.graphics.PorterDuff.Mode.SRC_IN);
            int numberOfChildren = linearLayout.getChildCount();
            for(int i=0; i<numberOfChildren; i++){
                ((TextView)linearLayout.getChildAt(i).findViewById(R.id.bottom_icon_TV)).setTextColor(Color.parseColor("#5f6267"));
            }
            //set current view as selected (color primary)
            ((ImageView)v).setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
            ((TextView)((RelativeLayout) v.getParent()).findViewById(R.id.bottom_icon_TV)).setTextColor(getResources().getColor(R.color.colorPrimary));
            if(v.getId() == R.id.history){

            }else if(v.getId() == R.id.schedule){

            }else if(v.getId() == R.id.workout){

            }else if(v.getId() == R.id.exercises){
                Intent intent = new Intent(MainActivity.this, InfoView.class);
                startActivity(intent);


            }else if(v.getId() == R.id.progress){

            }

        }
    }
}
