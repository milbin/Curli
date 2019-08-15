package com.fitness.curli;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WorkoutBuilder extends AppCompatActivity {
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_builder);
        System.out.println("HERE");
        findViewById(R.id.back_button).setOnClickListener(new onBackButtonClicked());
        ((TextView)findViewById(R.id.title)).setText("Create Workout");
        findViewById(R.id.fab).setOnClickListener(new onFabClick());
        //create finish button
        Button finishButton = new Button(this);
        finishButton.setText("FINISH");
        finishButton.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        finishButton.setOnClickListener(new onFinishClick());
        RelativeLayout.LayoutParams finishButtonLP = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        finishButtonLP.addRule(RelativeLayout.ALIGN_PARENT_END);
        finishButtonLP.addRule(RelativeLayout.CENTER_VERTICAL);
        finishButton.setLayoutParams(finishButtonLP);
        finishButton.setBackgroundColor(Color.TRANSPARENT);
        ((RelativeLayout)findViewById(R.id.toolbar_rl)).addView(finishButton);


        //this is currently just placeholder while we wait for theo to finish the info view,
        //the exercise cards are going to be programatically added instead of hardcoded

        findViewById(R.id.add_set_button).setOnClickListener(new AddSetButton());
        findViewById(R.id.remove_set_button).setOnClickListener(new RemoveSetButton());


    }
    public class onFinishClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
            builder.setTitle("Finish Building Workout?").setMessage("Are you sure you want to finish building this workout? It will be added to your workouts list.");
            // Add the buttons
            builder.setPositiveButton("Finish", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    //TODO add some sort of mechanism that saves newly created workouts
                    onBackPressed();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            // Create the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();


        }
    }

    public class onBackButtonClicked implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
            builder.setTitle("Discard Workout?").setMessage("Are you sure you want to discard this workout? This action is irreversible.");
            // Add the buttons
            builder.setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    onBackPressed();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            // Create the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();
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
