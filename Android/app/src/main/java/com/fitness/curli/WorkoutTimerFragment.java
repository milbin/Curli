package com.fitness.curli;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WorkoutTimerFragment extends Fragment {
    View fragment;
    Context context;
    HashMap currentWorkout;
    WorkoutTimer timer;
    Boolean isExpanded;

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
        isExpanded = bundle.getBoolean("isExpanded");

        //check if timer already exists
        System.out.println(((Curli) getActivity().getApplication()).getWorkoutTimer() == null);
        System.out.println("HERE");
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("ongoing workout", 0); // 0 - for private mode
        if(((Curli) getActivity().getApplication()).getWorkoutTimer() == null) {
            long startTime = pref.getLong("startTime", System.currentTimeMillis());
            timer = new WorkoutTimer();
            timer.setTextView((TextView) fragment.findViewById(R.id.timer));
            timer.resumeTimer(startTime);
            ((Curli) getActivity().getApplication()).setWorkoutTimer(timer);
            SharedPreferences.Editor editor = pref.edit();
            editor.putLong("startTime", startTime);
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
                        editor.putString("startTime", null);
                        editor.apply();
                        String time = (String) ((TextView) fragment.findViewById(R.id.timer)).getText();
                        currentWorkout.put("time", time);
                        ((Curli) getActivity().getApplication()).getWorkoutTimer().stopTimer();
                        ((Curli) getActivity().getApplication()).setWorkoutTimer(null);
                        SQLData sqlData = new SQLData();
                        sqlData.openUserDB(context);
                        sqlData.saveWorkoutToHistory(currentWorkout);
                        sqlData.closeDB();
                        getActivity().finish();
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


}
