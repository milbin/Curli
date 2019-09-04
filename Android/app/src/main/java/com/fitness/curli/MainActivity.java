package com.fitness.curli;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity{
    Context context = this;
    private Menu optionsMenu;
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
        sqlData.createTablesNew();

        HashMap json = new HashMap();
        HashMap backExtension = new HashMap();
        backExtension.put("title", "Back Extension Machine");
        backExtension.put("weight", 0.0);
        backExtension.put("reps", 0);
        json.put("exercises", new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList(0, backExtension)))));
        json.put("title", "Theo\u0027s workout");
        //sqlData.createUser("1080", "theo", "vasile", (float)(178.0), Calendar.getInstance().getTime());
        sqlData.saveWorkoutNew(json);
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


        //set toolbar onclick listener
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new onFabClick());


    }



    @Override
    public void onResume() {

        //check if app was closed while workout was still in progress
        SharedPreferences pref = getApplicationContext().getSharedPreferences("ongoing workout", 0); // 0 - for private mode
        String workoutState = pref.getString("workout", "noWorkout");
        if(!workoutState.equals("noWorkout")) {
            Gson gson = new Gson();
            java.lang.reflect.Type type = new TypeToken<HashMap<String, Object>>(){}.getType();
            HashMap currentWorkout = gson.fromJson(workoutState, type);
            FragmentManager mFragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            WorkoutTimerFragment fr = new WorkoutTimerFragment(); // Replace with your Fragment class
            Bundle bundle = new Bundle();
            bundle.putSerializable("currentWorkout",currentWorkout);
            bundle.putBoolean("isExpanded", false);
            fr.setArguments(bundle);
            fragmentTransaction.replace(R.id.ongoing_workout_toolbar, fr).commit();
            findViewById(R.id.ongoing_workout_toolbar).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.ongoing_workout_toolbar).setVisibility(View.GONE);
        }
        super.onResume();

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
