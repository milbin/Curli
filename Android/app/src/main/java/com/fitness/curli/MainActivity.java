package com.fitness.curli;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity {
    Context context = this;
    private Menu optionsMenu;
    HashMap currentWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RelativeLayout workoutPlaceholder = findViewById(R.id.workout_card);
        workoutPlaceholder.setOnClickListener(new onWorkoutClick());

        //set on click listener for the bottom bar buttons
        ((View)findViewById(R.id.history).getParent()).setOnClickListener(new onNavbarClick());
        ((View)findViewById(R.id.schedule).getParent()).setOnClickListener(new onNavbarClick());
        ((View)findViewById(R.id.workout).getParent()).setOnClickListener(new onNavbarClick());
        ((View)findViewById(R.id.exercises).getParent()).setOnClickListener(new onNavbarClick());
        ((View)findViewById(R.id.progress).getParent()).setOnClickListener(new onNavbarClick());
        ((View)findViewById(R.id.workout).getParent()).performClick();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(((Curli) this.getApplication()).getWorkoutTimer() != null){
            SharedPreferences pref = getApplicationContext().getSharedPreferences("ongoing workout", 0); // 0 - for private mode
            String workoutString = pref.getString("workout", "FAIL");
            Gson gson = new Gson();
            java.lang.reflect.Type type = new TypeToken<HashMap<String, Object>>(){}.getType();
            currentWorkout = gson.fromJson(workoutString, type);
            findViewById(R.id.ongoing_workout_toolbar).setVisibility(View.VISIBLE);
            findViewById(R.id.down_arrow).setVisibility(View.GONE);
            findViewById(R.id.finish).setVisibility(View.GONE);
            findViewById(R.id.up_arrow).setVisibility(View.VISIBLE);
            findViewById(R.id.delete).setVisibility(View.VISIBLE);
            findViewById(R.id.delete).setOnClickListener(new onCancelWorkout());
            ((TextView)findViewById(R.id.ongoing_workout_title)).setText((String) currentWorkout.get("title"));
            WorkoutTimer timer = ((Curli) this.getApplication()).getWorkoutTimer();
            timer.setTextView((TextView)findViewById(R.id.timer));
            findViewById(R.id.up_arrow).setOnClickListener(new onResumeWorkout());
            findViewById(R.id.ongoing_workout_toolbar).setOnClickListener(new onResumeWorkout());
        }


    }

    public class onResumeWorkout implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(MainActivity.this, WorkoutActivity.class);
            myIntent.putExtra("workout", currentWorkout);
            startActivity(myIntent);
        }
    }
    public class onCancelWorkout implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            final View view  = v;
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
            builder.setTitle("Delete Workout?").setMessage("This action cannot be undone.");
            // Add the buttons
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("ongoing workout", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("workout", null);
                    editor.apply();
                    ((Curli) getApplication()).setWorkoutTimer(null);
                    ((Toolbar)view.getParent().getParent()).setVisibility(View.GONE);
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




    public class onWorkoutClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if(currentWorkout != null){
                Intent myIntent = new Intent(MainActivity.this, WorkoutActivity.class);
                myIntent.putExtra("workout", currentWorkout);
                startActivity(myIntent);
            }else {

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
                Intent myIntent = new Intent(MainActivity.this, WorkoutActivity.class);
                myIntent.putExtra("workout", workout);
                startActivity(myIntent);

                System.out.println(workout);
            }

        }
    }

    public class onNavbarClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //grey out all other icons and textviews
            LinearLayout linearLayout = (LinearLayout)v.getParent();
            ((ImageView)findViewById(R.id.history)).setColorFilter(Color.parseColor("#5f6267"), android.graphics.PorterDuff.Mode.SRC_IN);
            ((ImageView)findViewById(R.id.schedule)).setColorFilter(Color.parseColor("#5f6267"), android.graphics.PorterDuff.Mode.SRC_IN);
            ((ImageView)findViewById(R.id.workout)).setColorFilter(Color.parseColor("#5f6267"), android.graphics.PorterDuff.Mode.SRC_IN);
            ((ImageView)findViewById(R.id.exercises)).setColorFilter(Color.parseColor("#5f6267"), android.graphics.PorterDuff.Mode.SRC_IN);
            ((ImageView)findViewById(R.id.progress)).setColorFilter(Color.parseColor("#5f6267"), android.graphics.PorterDuff.Mode.SRC_IN);
            int numberOfChildren = linearLayout.getChildCount();
            for(int i=0; i<numberOfChildren; i++){
                ((TextView)linearLayout.getChildAt(i).findViewById(R.id.bottom_icon_TV)).setTextColor(Color.parseColor("#5f6267"));
            }

            if(v.findViewById(R.id.history) != null){
                //set current view as selected (color primary)
                ((ImageView)v.findViewById(R.id.history)).setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                ((TextView)v.findViewById(R.id.bottom_icon_TV)).setTextColor(getResources().getColor(R.color.colorPrimary));

            }else if(v.findViewById(R.id.schedule) != null){
                //set current view as selected (color primary)
                ((ImageView)v.findViewById(R.id.schedule)).setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                ((TextView)v.findViewById(R.id.bottom_icon_TV)).setTextColor(getResources().getColor(R.color.colorPrimary));

            }else if(v.findViewById(R.id.workout)!= null){
                //set current view as selected (color primary)
                ((ImageView)v.findViewById(R.id.workout)).setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                ((TextView)v.findViewById(R.id.bottom_icon_TV)).setTextColor(getResources().getColor(R.color.colorPrimary));

            }else if(v.findViewById(R.id.exercises)!= null){
                //set current view as selected (color primary)
                ((ImageView)v.findViewById(R.id.exercises)).setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                ((TextView)v.findViewById(R.id.bottom_icon_TV)).setTextColor(getResources().getColor(R.color.colorPrimary));
                Intent intent = new Intent(MainActivity.this, InfoView.class);
                startActivity(intent);
                overridePendingTransition(0, 0); //this disables animations


            }else if(v.findViewById(R.id.progress)!= null){
                //set current view as selected (color primary)
                ((ImageView)v.findViewById(R.id.progress)).setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                ((TextView)v.findViewById(R.id.bottom_icon_TV)).setTextColor(getResources().getColor(R.color.colorPrimary));

            }

        }
    }
}
