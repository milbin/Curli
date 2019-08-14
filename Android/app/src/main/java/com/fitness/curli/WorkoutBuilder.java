package com.fitness.curli;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WorkoutBuilder extends AppCompatActivity {
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_builder);
        System.out.println("HERE");
        findViewById(R.id.back_button).setOnClickListener(new onBackButtonPressed());
        ((TextView)findViewById(R.id.title)).setText("Create Workout");
        findViewById(R.id.fab).setOnClickListener(new onFabClick());


        //this is currently just placeholder while we wait for theo to finish the info view,
        //the exercise cards are going to be programatically added instead of hardcoded

        findViewById(R.id.add_set_button).setOnClickListener(new AddSetButton());
        findViewById(R.id.remove_set_button).setOnClickListener(new RemoveSetButton());


    }

    public class onBackButtonPressed implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    }

    public class onFabClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }
    public class AddSetButton implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            View weightAndReps = LayoutInflater.from(context).inflate(R.layout.weight_and_reps, null);
            LinearLayout ll = ((View)v.getParent()).findViewById(R.id.weight_and_reps_ll);
            ll.addView(weightAndReps);

        }
    }
    public class RemoveSetButton implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            LinearLayout ll = ((View)v.getParent()).findViewById(R.id.weight_and_reps_ll);
            if(ll.getChildCount() != 1) {
                ll.removeViewAt(ll.getChildCount() - 1);
            }

        }
    }
}
