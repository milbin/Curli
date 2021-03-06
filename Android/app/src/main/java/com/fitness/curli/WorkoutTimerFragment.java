package com.fitness.curli;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class WorkoutTimerFragment extends Fragment {
    View fragment;
    Context context;
    HashMap currentWorkout;
    WorkoutTimer timer;
    Boolean isExpanded;
    int workoutNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.ongoing_workout, container, false);
        fragment.findViewById(R.id.down_arrow).setOnClickListener(new onPauseWorkout());
        fragment.findViewById(R.id.delete).setOnClickListener(new onCancelWorkout());
        fragment.setOnClickListener(new onPauseWorkout());
        fragment.findViewById(R.id.finish).setOnClickListener(new onWorkoutFinished());
        context = getActivity();


        Bundle bundle = this.getArguments();

        currentWorkout = (HashMap) bundle.getSerializable("currentWorkout");
        workoutNumber = bundle.getInt("workoutNumber");
        isExpanded = bundle.getBoolean("isExpanded");
        ((TextView)fragment.findViewById(R.id.ongoing_workout_title)).setText((String)currentWorkout.get("title"));

        //check if timer already exists
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("ongoing workout", 0); // 0 - for private mode
        if(((Curli) getActivity().getApplication()).getWorkoutTimer() == null) {
            long startTime = pref.getLong("startTime", System.currentTimeMillis());
            timer = new WorkoutTimer();
            timer.setTextView((TextView) fragment.findViewById(R.id.timer));
            timer.resumeTimer(startTime);
            ((Curli) getActivity().getApplication()).setWorkoutTimer(timer);
            SharedPreferences.Editor editor = pref.edit();
            editor.putLong("startTime", startTime);
            editor.putInt("workoutNumber", workoutNumber);
            editor.apply();
        }else{
            long startTime = pref.getLong("startTime", System.currentTimeMillis());
            //setup timer
            timer = new WorkoutTimer();
            timer.setTextView((TextView) fragment.findViewById(R.id.timer));
            timer.resumeTimer(startTime);
            ((Curli) getActivity().getApplication()).setWorkoutTimer(timer);
        }

        //check if fragment is being created in the workout activity or outside of it
        if(!isExpanded){
            fragment.findViewById(R.id.down_arrow).setVisibility(View.GONE);
            fragment.findViewById(R.id.finish).setVisibility(View.GONE);
            fragment.findViewById(R.id.up_arrow).setVisibility(View.VISIBLE);
            fragment.findViewById(R.id.delete).setVisibility(View.VISIBLE);
            fragment.findViewById(R.id.divider_line1).setVisibility(View.GONE);//hide the divider line for the ongoing workout
            fragment.setOnClickListener(new onResumeWorkout());
            fragment.findViewById(R.id.up_arrow).setOnClickListener(new onResumeWorkout());
        }


        return fragment;
    }


    public class onPauseWorkout implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //save workout
            SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("ongoing workout", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            Gson gson = new Gson();
            String hashMapString = gson.toJson(currentWorkout);
            editor.putString("workout", hashMapString);
            editor.apply();
            ((Curli) getActivity().getApplication()).setWorkout(currentWorkout);
            getActivity().finish();


        }
    }

    public class onResumeWorkout implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(getActivity(), WorkoutActivity.class);
            myIntent.putExtra("workout", currentWorkout);
            myIntent.putExtra("workoutNumber", workoutNumber);
            startActivity(myIntent);
        }
    }
    public class onCancelWorkout implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            final View view  = v;
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
            builder.setTitle("Discard Workout?").setMessage("This action cannot be undone.");
            // Add the buttons
            builder.setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("ongoing workout", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("workout", null);
                    editor.putInt("workoutNumber", -1);
                    editor.putString("startTime", null);
                    editor.apply();
                    ((Curli) getActivity().getApplication()).getWorkoutTimer().stopTimer();
                    ((Curli) getActivity().getApplication()).setWorkoutTimer(null);
                    ((Toolbar)view.getParent().getParent()).setVisibility(View.GONE);
                    currentWorkout = null;
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

    private class onWorkoutFinished implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int totalSets = 0;
            for (ArrayList exercise : (ArrayList<ArrayList>)currentWorkout.get("exercises")) {
                totalSets += (int) exercise.get(0);
            }
            if (totalSets <= 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
                builder.setTitle("Cant Finish Workout!").setMessage("You need to complete a set before finishing your workout.");
                // Add the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
                builder.setTitle("Finish Workout?").setMessage("Are you sure you want to finish this workout?");
                // Add the buttons
                builder.setPositiveButton("Finish", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("ongoing workout", 0); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("workout", null);
                        editor.putInt("workoutNumber", -1);
                        editor.putString("startTime", null);
                        editor.apply();
                        final String time = (String) ((TextView) fragment.findViewById(R.id.timer)).getText();
                        ((Curli) getActivity().getApplication()).getWorkoutTimer().stopTimer();
                        ((Curli) getActivity().getApplication()).setWorkoutTimer(null);
                        final SQLData sqlData = new SQLData();
                        sqlData.openUserDB(context);

                        Gson gson = new Gson();
                        String jsonString = gson.toJson(currentWorkout);
                        java.lang.reflect.Type type = new TypeToken<HashMap<String, Object>>(){}.getType();
                        final HashMap workoutToCompare = gson.fromJson(jsonString, type);
                        removeSets(workoutToCompare);

                        currentWorkout.put("time", time);
                        sqlData.saveWorkoutToHistory(currentWorkout);




                        if(!workoutToCompare.equals(sqlData.getworkout(workoutNumber).get(1))){ //workout was changed by user
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
                            builder1.setTitle("Update Workout?").setMessage("You modified this workout. Would you like to save these changes?");
                            // Add the buttons
                            builder1.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    sqlData.updateWorkout(workoutNumber, workoutToCompare);
                                    sqlData.closeDB();
                                    getActivity().finish();
                                }
                            });
                            builder1.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    sqlData.closeDB();
                                    getActivity().finish();
                                }
                            });
                            builder1.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    sqlData.closeDB();
                                    getActivity().finish();
                                }
                            });
                            AlertDialog dialog1 = builder1.create();
                            dialog1.show();

                        }else{
                            sqlData.closeDB();
                            getActivity().finish();
                        }

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
    }

    private void removeSets(HashMap workout){
        for (ArrayList exercise : (ArrayList<ArrayList>)workout.get("exercises")) {
           exercise.set(0, 0.0);
        }
    }


}
