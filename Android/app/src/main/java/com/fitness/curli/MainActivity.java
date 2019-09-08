package com.fitness.curli;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
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
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity{
    Context context = this;
    private Menu optionsMenu;
    int RESULT_FINISHED_BUILD = 1;
    public int RESULT_WORKOUT_BUILDER_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar( (Toolbar) findViewById(R.id.toolbar));

        //add workout cards programatically
        SQLData sqlData = new SQLData();
        sqlData.openUserDB(this);
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

        findViewById(R.id.profile_button).setOnClickListener(new onProfileClick());
        //set toolbar onclick listener
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new onFabClick());


    }

    public class onProfileClick implements View.OnClickListener {

        @Override
        public void onClick(View v){
            Intent myIntent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(myIntent);
        }
    }



    @Override
    public void onResume() {

        //check if app was closed while workout was still in progress
        SharedPreferences pref = getApplicationContext().getSharedPreferences("ongoing workout", 0); // 0 - for private mode
        String workoutState = pref.getString("workout", "noWorkout");
        int workoutNumber = pref.getInt("workoutNumber", -1);
        if(!workoutState.equals("noWorkout")) {
            Gson gson = new Gson();
            java.lang.reflect.Type type = new TypeToken<HashMap<String, Object>>(){}.getType();
            HashMap currentWorkout = gson.fromJson(workoutState, type);
            FragmentManager mFragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            WorkoutTimerFragment fr = new WorkoutTimerFragment(); // Replace with your Fragment class
            Bundle bundle = new Bundle();
            bundle.putSerializable("currentWorkout",currentWorkout);
            bundle.putInt("workoutNumber",workoutNumber);
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
                //add workout cards programatically
                SQLData sqlData = new SQLData();
                sqlData.openUserDB(this);
                SQLData sqlDataExercise = new SQLData();
                sqlDataExercise.openExerciseDB(this);
                LinearLayout ll = findViewById(R.id.linearLayoutMain);
                ll.removeAllViews();
                int workoutCount = sqlData.getWorkoutCount();
                for(int i=0; i<workoutCount; i++){
                    RelativeLayout card = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.workout_card, null);
                    ArrayList workoutTemp = sqlData.getworkout(i);
                    ((TextView)card.findViewById(R.id.title)).setText((String)workoutTemp.get(0));
                    ll.addView(card);
                    ArrayList<ArrayList> exercises = (ArrayList<ArrayList>) ((HashMap)workoutTemp.get(1)).get("exercises");
                    setupWorkoutCard(card, exercises);
                }
                sqlData.closeDB();

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
        //calculate total reps and sets in order to get the estimated time to complete the workout
        int totalReps = 0;
        int totalSets = 0;
        int totalExercises = 0;
        ArrayList<String> equipmentList = new ArrayList<String>();
        ArrayList<String> muscleList = new ArrayList<>();
        LinearLayout muscleLL = card.findViewById(R.id.MuscleGroupLL);
        for(ArrayList exercise:exercises){
            for(int setNum=1;setNum<exercise.size();setNum++){
                Map set = (Map) exercise.get(setNum);
                String exerciseName = (String)set.get("title");

                //add equipment to the equipment TextView
                String equipmentString = sqlDataExercise.getPrimaryEquipmentFromName(exerciseName);
                for(String equipment: equipmentString.split(", ")){
                    if(!equipmentList.contains(equipment)){
                        equipmentList.add(equipment);
                    }
                }
                //add muscle icons to the muscle LinearLayout
                String muscleName = sqlDataExercise.getGroup1FromName(exerciseName);
                if(!muscleList.contains(muscleName)){
                    muscleList.add(muscleName);
                    ImageView muscleIcon = new ImageView(context);
                    muscleIcon.setImageDrawable(getDrawable(R.drawable.abs));
                    if(muscleName.equals("Abs")){
                        muscleIcon.setImageDrawable(getDrawable(R.drawable.abs));
                    }else if(muscleName.equals("Back")){
                        muscleIcon.setImageDrawable(getDrawable(R.drawable.abs));
                    }else if(muscleName.equals("Bicep")){
                        muscleIcon.setImageDrawable(getDrawable(R.drawable.abs));
                    }else if(muscleName.equals("Chest")){
                        muscleIcon.setImageDrawable(getDrawable(R.drawable.chest));
                    }else if(muscleName.equals("Forearm")){
                        muscleIcon.setImageDrawable(getDrawable(R.drawable.abs));
                    }else if(muscleName.equals("Glutes")){
                        muscleIcon.setImageDrawable(getDrawable(R.drawable.glutes));
                    }else if(muscleName.equals("Lower Legs")){
                        muscleIcon.setImageDrawable(getDrawable(R.drawable.lower_legs));
                    }else if(muscleName.equals("Shoulder")){
                        muscleIcon.setImageDrawable(getDrawable(R.drawable.abs));
                    }else if(muscleName.equals("Triceps")){
                        muscleIcon.setImageDrawable(getDrawable(R.drawable.triceps));
                    }else if(muscleName.equals("Upper Legs")){
                        muscleIcon.setImageDrawable(getDrawable(R.drawable.upper_legs));
                    }
                    //convert from dp to px
                    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                    float dp = 30f;
                    float fpixels = metrics.density * dp;
                    int pixels = (int) (fpixels + 0.5f);
                    muscleIcon.setScaleType(ImageView.ScaleType.FIT_XY);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(pixels,pixels);
                    muscleIcon.setLayoutParams(lp);
                    muscleLL.addView(muscleIcon);

                }
                totalReps += (int)set.get("reps");
                totalSets++;
            }
            totalExercises++;
        }
        //concatenate the equipment list together to create the final TV string
        String finalEquipmentString = "";
        for(String equipment: equipmentList){
            if(!equipment.equals("None") && !equipment.equals("")) {
                finalEquipmentString += equipment + " · ";
            }
        }
        finalEquipmentString = finalEquipmentString.substring(0, finalEquipmentString.length() - 3);
        ((TextView)card.findViewById(R.id.time)).setText((((totalReps*5)+(totalSets*60))/60)+" mins"); //TODO change the 60 second rest time to the rest period of the user defined in their profile
        if(totalExercises == 1){
            ((TextView)card.findViewById(R.id.number_of_exercises)).setText(totalExercises +" Exercises");
        }else {
            ((TextView) card.findViewById(R.id.number_of_exercises)).setText(totalExercises + " Exercise");
        }
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
            final int workoutNumberFinal = workoutNumber;
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
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("ongoing workout", 0); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("workout", null);
                        editor.putInt("workoutNumber", -1);
                        editor.putString("startTime", null);
                        editor.apply();

                        Intent myIntent = new Intent(MainActivity.this, WorkoutActivity.class);
                        myIntent.putExtra("workout", workout);
                        myIntent.putExtra("workoutNumber", workoutNumberFinal);
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
                myIntent.putExtra("workoutNumber", workoutNumberFinal);
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
                    if(item.getItemId() == R.id.workout_overflow_edit) {
                        Intent myIntent = new Intent(MainActivity.this, WorkoutBuilder.class);
                        sqlData.openUserDB(context);
                        ArrayList workout = sqlData.getworkout(workoutNumber);
                        myIntent.putExtra("workout", (HashMap) workout.get(1));
                        myIntent.putExtra("workoutNumber", workoutNumber);
                        startActivityForResult(myIntent, 1);
                        sqlData.closeDB();
                    }else if(item.getItemId() == R.id.workout_overflow_duplicate) {
                        sqlData.openUserDB(context);
                        ArrayList workoutToDuplicate = sqlData.getworkout(workoutNumber);
                        String newTitle = "Workout " + (sqlData.getWorkoutCount() + 1);
                        workoutToDuplicate.set(0, newTitle);
                        ((HashMap) workoutToDuplicate.get(1)).put("title", newTitle);
                        sqlData.saveWorkout((HashMap) workoutToDuplicate.get(1));
                        RelativeLayout card = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.workout_card, null);
                        ArrayList<ArrayList> exercises = (ArrayList<ArrayList>) ((HashMap) workoutToDuplicate.get(1)).get("exercises");
                        ll.addView(card);
                        ((TextView) card.findViewById(R.id.title)).setText(newTitle);
                        setupWorkoutCard(card, exercises);
                        sqlData.closeDB();

                    }else if(item.getItemId() == R.id.workout_overflow_delete) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
                        builder.setTitle("Are you sure you want to delete this workout?").setMessage("This action cannot be undone.");
                        // Add the buttons
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                sqlData.openUserDB(context);
                                System.out.println(workoutNumber);
                                sqlData.deleteWorkout(workoutNumber);
                                ll.removeViewAt(workoutNumber);
                                if (sqlData.getWorkoutCount() == 0) {
                                    findViewById(R.id.no_workout_tv).setVisibility(View.VISIBLE);
                                }
                                sqlData.closeDB();
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

                     return true;
                 }
             }
            );
        }
    }



}
