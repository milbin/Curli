package com.fitness.curli;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity{
    Context context = this;
    private Menu optionsMenu;
    HashMap currentWorkout;
    int RESULT_FINISHED_BUILD = 1;
    public int RESULT_WORKOUT_BUILDER_ACTIVITY = 1;
    int workoutToRemove = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar( (Toolbar) findViewById(R.id.toolbar));
        //add workout cards programatically
        SQLData sqlData = new SQLData();
        sqlData.openUserDB(this);
        SQLData sqlDataExercise = new SQLData();
        sqlDataExercise.openExerciseDB(this);
        int workoutCount = sqlData.getWorkoutCount();

        for(int i=0; i<workoutCount; i++){
            RelativeLayout card = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.workout_card, null);
            ArrayList workoutTemp = sqlData.getworkout(i);
            ((TextView)card.findViewById(R.id.title)).setText((String)workoutTemp.get(0));
            LinearLayout ll = findViewById(R.id.linearLayoutMain);
            ll.addView(card);
            ArrayList<ArrayList> exercises = (ArrayList<ArrayList>) ((HashMap)workoutTemp.get(1)).get("exercises");
            setupWorkoutCard(card, exercises);
        }
        sqlData.closeDB();


        //set on click listener for the bottom bar buttons
        ((View)findViewById(R.id.history).getParent()).setOnClickListener(new onNavbarClick());
        ((View)findViewById(R.id.schedule).getParent()).setOnClickListener(new onNavbarClick());
        ((View)findViewById(R.id.workout).getParent()).setOnClickListener(new onNavbarClick());
        ((View)findViewById(R.id.exercises).getParent()).setOnClickListener(new onNavbarClick());
        ((View)findViewById(R.id.progress).getParent()).setOnClickListener(new onNavbarClick());
        ((View)findViewById(R.id.workout).getParent()).performClick();

        //set toolbar onclick listener

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new onFabClick());




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == RESULT_WORKOUT_BUILDER_ACTIVITY) {
            if(requestCode == RESULT_FINISHED_BUILD){
                LinearLayout ll = findViewById(R.id.linearLayoutMain);
                SQLData sqlDataExercise = new SQLData();
                sqlDataExercise.openExerciseDB(this);
                HashMap newWorkout = (HashMap) data.getSerializableExtra("workout");

                RelativeLayout card = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.workout_card, null);
                ((TextView)card.findViewById(R.id.title)).setText((String)newWorkout.get("title"));
                if(workoutToRemove != -1){ //if the workout returned form being edited, delete the old once and replace with new one
                    ll.removeViewAt(workoutToRemove);
                    workoutToRemove = -1;
                    ll.addView(card, workoutToRemove);
                }else{//this is a new workout
                    ll.addView(card);
                }
                ArrayList<ArrayList> exercises = (ArrayList<ArrayList>) newWorkout.get("exercises");
                setupWorkoutCard(card, exercises);

            }
        }
    }

    private void setupWorkoutCard(RelativeLayout card, ArrayList<ArrayList> exercises){
        card.setOnClickListener(new onWorkoutClick());
        ImageButton overflowButton = card.findViewById(R.id.overflowButton);
        overflowButton.setOnClickListener(new onOverflowClick());
        SQLData sqlDataExercise = new SQLData();
        sqlDataExercise.openExerciseDB(this);
        //convert double reps to int
        int count = 0;
        for(ArrayList<Object> exercise:exercises) {
            int count1 = 0;
            for(Object set:exercise) {
                if (count1 != 0) {
                    try {
                    ((Map)set).put("reps", (int)Math.round((double)((Map)set).get("reps")));
                    }catch (ClassCastException e){}//catch casting error if reps are already a int
                    exercise.set(count1, set);
                }else{
                    try {
                        exercise.set(0, (int) Math.round((double) set));
                    }catch (ClassCastException e){}//catch casting error if set is already a int
                }
                count1++;
            }
            exercises.set(count, exercise);
            count++;
        }
        int totalReps = 0;
        int totalSets = 0;
        int totalExercises = 0;
        ArrayList<String> equipmentList = new ArrayList<String>();
        for(ArrayList exercise:exercises){
            for(int setNum=1;setNum<exercise.size();setNum++){
                Map set = (Map) exercise.get(setNum);
                String equipmentString = sqlDataExercise.getEquipmentFromName((String)set.get("title"));
                for(String equipment: equipmentString.split(", ")){
                    if(!equipmentList.contains(equipment)){
                        equipmentList.add(equipment);
                    }
                }
                totalReps += (int)set.get("reps");
                totalSets++;
            }
            totalExercises++;
        }
        String finalEquipmentString = "";
        for(String equipment: equipmentList){
            finalEquipmentString += equipment+" · ";
        }
        finalEquipmentString = finalEquipmentString.substring(0, finalEquipmentString.length() - 3);
        ((TextView)card.findViewById(R.id.time)).setText((((totalReps*5)+(totalSets*60))/60)+" mins"); //TODO change the 60 second rest time to the rest period of the user defined in their profile
        ((TextView)card.findViewById(R.id.number_of_exercises)).setText(totalExercises +" Exercises");
        ((TextView)card.findViewById(R.id.equipment)).setText(finalEquipmentString);
        findViewById(R.id.no_workout_tv).setVisibility(View.GONE);

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
            findViewById(R.id.divider_line1).setVisibility(View.GONE);//hide the divider line for the ongoing workout
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
                    ((Curli) getApplication()).getWorkoutTimer().stopTimer();
                    ((Curli) getApplication()).setWorkoutTimer(null);
                    currentWorkout = null;
                    ((Toolbar)view.getParent().getParent()).setVisibility(View.GONE);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialogs
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
            LinearLayout ll = (LinearLayout) v.getParent();
            int workoutNumber = 0;
            for(int i=0; i<ll.getChildCount();i++ ){
                if(v == ll.getChildAt(i)){
                    workoutNumber = i;
                }
            }
            SQLData sqlData = new SQLData();
            sqlData.openUserDB(context);
            ArrayList workoutList = sqlData.getworkout(workoutNumber);
            sqlData.closeDB();
            final HashMap workout = (HashMap) workoutList.get(1);

            if(((Curli) getApplication()).getWorkoutTimer() != null){
                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
                builder.setTitle("Discard Current Workout?").setMessage("Starting a new workout will discard your old one.");
                // Add the buttons
                builder.setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((Curli) getApplication()).getWorkoutTimer().stopTimer();
                        ((Curli) getApplication()).setWorkoutTimer(null);

                        Intent myIntent = new Intent(MainActivity.this, WorkoutActivity.class);
                        myIntent.putExtra("workout", workout);
                        startActivity(myIntent);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialogs
                    }
                });
                // Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();

            }else {
                Intent myIntent = new Intent(MainActivity.this, WorkoutActivity.class);
                myIntent.putExtra("workout", workout);
                startActivity(myIntent);
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
                Intent intent = new Intent(MainActivity.this, MuscleView.class);
                startActivity(intent);
                overridePendingTransition(0, 0); //this disables animations


            }else if(v.findViewById(R.id.progress)!= null){
                //set current view as selected (color primary)
                ((ImageView)v.findViewById(R.id.progress)).setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                ((TextView)v.findViewById(R.id.bottom_icon_TV)).setTextColor(getResources().getColor(R.color.colorPrimary));

            }

        }
    }

    public class onFabClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(MainActivity.this, WorkoutBuilder.class);
            startActivityForResult(myIntent, 1);
        }
    }

    private class onOverflowClick implements View.OnClickListener {
        @Override
        public void onClick(final View v) {

            final LinearLayout ll = (LinearLayout) v.getParent().getParent().getParent();
            int number = 0;
            for(int i=0; i<ll.getChildCount();i++ ){
                if(v.getParent().getParent() == ll.getChildAt(i)){
                    number = i;
                }
            }
            final int workoutNumber = number;
            final SQLData sqlData = new SQLData();
            // This is an android.support.v7.widget.PopupMenu;
            PopupMenu popupMenu = new PopupMenu(context, v);
            popupMenu.inflate(R.menu.workout_overflow_menu);
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
                 @Override
                 public boolean onMenuItemClick(MenuItem item) {
                     switch (item.getItemId()) {
                         case R.id.workout_overflow_edit:

                             Intent myIntent = new Intent(MainActivity.this, WorkoutBuilder.class);
                             sqlData.openUserDB(context);
                             ArrayList workout = sqlData.getworkout(workoutNumber);
                             myIntent.putExtra("workout", (HashMap)workout.get(1));
                             myIntent.putExtra("workoutNumber", workoutNumber);
                             workoutToRemove = workoutNumber;
                             startActivityForResult(myIntent, 1);
                             return true;

                         case R.id.workout_overflow_duplicate:
                             sqlData.openUserDB(context);
                             ArrayList workoutToDuplicate = sqlData.getworkout(workoutNumber);
                             String newTitle = "Workout "+ (sqlData.getWorkoutCount()+1);
                             workoutToDuplicate.set(0, newTitle);
                             ((HashMap)workoutToDuplicate.get(1)).put("title", newTitle);
                             sqlData.saveWorkout((HashMap)workoutToDuplicate.get(1));
                             RelativeLayout card = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.workout_card, null);
                             ArrayList<ArrayList> exercises = (ArrayList<ArrayList>) ((HashMap)workoutToDuplicate.get(1)).get("exercises");
                             ll.addView(card);
                             ((TextView)card.findViewById(R.id.title)).setText(newTitle);
                             setupWorkoutCard(card, exercises);
                             return true;

                         case R.id.workout_overflow_delete:
                             AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
                             builder.setTitle("Are you sure you want to delete this workout?").setMessage("This action cannot be undone.");
                             // Add the buttons
                             builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int id) {
                                     sqlData.openUserDB(context);
                                     sqlData.deleteWorkout(workoutNumber);
                                     ll.removeViewAt(workoutNumber);
                                     if(sqlData.getWorkoutCount() == 0) {
                                         findViewById(R.id.no_workout_tv).setVisibility(View.VISIBLE);
                                     }
                                 }
                             });
                             builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int id) {
                                     // User cancelled the dialogs
                                 }
                             });
                             // Create the AlertDialog
                             AlertDialog dialog = builder.create();
                             dialog.show();



                             return true;

                         default:
                             return true;
                     }
                 }
             }
            );
        }
    }



}
