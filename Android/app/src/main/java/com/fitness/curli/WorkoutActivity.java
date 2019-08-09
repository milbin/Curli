package com.fitness.curli;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class WorkoutActivity extends AppCompatActivity {
    //WORKOUT DATA STRUCTURE EXAMPLE:
    //This is what the activity takes as input parameters, the reason that every set has a the name of the
    //exercise associated with it is so that we can create supersets easily later on
    //TODO create superset
    //{"title": "Arms and Chest", "exercises": [[0, {"title": "Bench Press", "weight":135, "reps":8},
    //{"title": "Bench Press", "weight":135, "reps":8}, {"title": "Bench Press", "weight":135, "reps":8}],
    //[0, {"title": "Bicep Curl", "weight":75, "reps":8}, {"title": "Bicep Curl", "weight":75, "reps":8},
    //{"title": "Bicep Curl", "weight":75, "reps":8}]]}

    ArrayList<ArrayList> exercises;
    Context context;
    int checkmark_size = 50;
    RelativeLayout currentlyExpandedCard;
    WorkoutTimer timer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_activity);
        LinearLayout linearLayout = findViewById(R.id.linearLayoutWorkout);
        context = this;

        HashMap workout = (HashMap) getIntent().getSerializableExtra("workout");


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setElevation(7);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        findViewById(R.id.logo).setVisibility(View.GONE);
        getSupportActionBar().setTitle(null);
        ((TextView)findViewById(R.id.title)).setText((String)workout.get("title"));

        SharedPreferences pref = getApplicationContext().getSharedPreferences("ongoing workout", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("title", (String)workout.get("title"));
        editor.apply();


        timer = new WorkoutTimer();
        timer.setTextView((TextView)findViewById(R.id.timer));
        timer.startTimer();
        ((Curli) this.getApplication()).setWorkoutTimer(timer);






        exercises = (ArrayList<ArrayList>) workout.get("exercises");
        int counter = 0;
        for(ArrayList<HashMap> exercise:exercises) {
            View relativeLayout = LayoutInflater.from(this).inflate(R.layout.exercise_card, null);
            linearLayout.addView(relativeLayout);
            relativeLayout.setOnClickListener(new onExpandClick());

            ImageButton overflowButton = relativeLayout.findViewById(R.id.overflowButton);
            overflowButton.setOnClickListener(new onOverflowClick(context));
            ImageButton weightAddButton = relativeLayout.findViewById(R.id.weight_add_button);
            weightAddButton.setOnClickListener(new onAddOrSubtractClick());
            ImageButton weightSubtractButton = relativeLayout.findViewById(R.id.weight_subtract_button);
            weightSubtractButton.setOnClickListener(new onAddOrSubtractClick());
            ImageButton repsAddButton = relativeLayout.findViewById(R.id.reps_add_button);
            repsAddButton.setOnClickListener(new onAddOrSubtractClick());
            ImageButton repsSubtractButton = relativeLayout.findViewById(R.id.reps_subtract_button);
            repsSubtractButton.setOnClickListener(new onAddOrSubtractClick());
            Button setBackButton = relativeLayout.findViewById(R.id.exercise_sets_back_button);
            setBackButton.setOnClickListener(new onDecrementSet());
            Button doneButton = relativeLayout.findViewById(R.id.done_button);
            doneButton.setOnClickListener(new onIncrementSet());

            TextView exerciseName = relativeLayout.findViewById(R.id.exercise_name);
            EditText exerciseReps = relativeLayout.findViewById(R.id.exercise_reps);
            EditText exerciseWeight = relativeLayout.findViewById(R.id.exercise_weight);
            TextView excerciseSets = relativeLayout.findViewById(R.id.set_number);
            View dividerLine = relativeLayout.findViewById(R.id.divider_line);

            exerciseName.setText((String) exercise.get(1).get("title"));
            exerciseReps.setText(Integer.toString((Integer) exercise.get(1).get("reps")));
            exerciseWeight.setText(Double.toString((Double) exercise.get(1).get("weight")));
            excerciseSets.setText("Sets Completed: " + 0 + " of " + (exercise.size() - 1));

            LinearLayout exerciseSets = relativeLayout.findViewById(R.id.checkbox_linear_layout);
            for (int i = 1; i < exercise.size(); i++) {
                ImageView checkbox = new ImageView(this);
                checkbox.setImageDrawable(getDrawable(R.drawable.ic_check_circle_grey_24dp));

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        checkmark_size,
                        checkmark_size
                );
                if (i != 1) {
                    params.setMarginStart(10);
                }
                checkbox.setLayoutParams(params);
                exerciseSets.addView(checkbox);
            }

            if(counter != 0){ //
                collapseCard((RelativeLayout) relativeLayout);
            }else{ //initlize the current expanded card with the first exercise
                currentlyExpandedCard = (RelativeLayout) relativeLayout;
            }
            counter++;
        }
    }


    public class onDecrementSet implements View.OnClickListener { //done button

        @Override
        public void onClick(View v) {
            LinearLayout linearLayout = (LinearLayout)(v.getParent()).getParent().getParent();
            int exerciseNumber = 0;
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                if(linearLayout.getChildAt(i) == ((View)v.getParent()).getParent()){
                    exerciseNumber = i;
                    break;
                }
            }



            ArrayList exercise = exercises.get(exerciseNumber);
            int setNumber = (int) exercise.get(0)-1;
            RelativeLayout relativeLayout = (RelativeLayout) v.getParent();
            System.out.println(setNumber);
            System.out.println(exercise);
            if(setNumber == -1){ //this is so that the set number does not go below zero
                return;
            }else{
                exercise.set(0, setNumber); //decrement the set number by 1
                LinearLayout checkboxLinearLayout = ((View) v.getParent()).findViewById(R.id.checkbox_linear_layout);
                for(int i=exercise.size()-2; i>=setNumber; i--) {
                    checkboxLinearLayout.getChildAt(i);
                    checkboxLinearLayout.removeViewAt(i);

                    ImageView checkbox = new ImageView(context);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            checkmark_size,
                            checkmark_size
                    );
                    if (i != 0) {
                        params.setMarginStart(10);
                    }
                    checkbox.setLayoutParams(params);
                    checkbox.setImageDrawable(getDrawable(R.drawable.ic_check_circle_grey_24dp));
                    checkboxLinearLayout.addView(checkbox, i);

                    TextView exerciseName = relativeLayout.findViewById(R.id.exercise_name);
                    exerciseName.setText((String) ((HashMap) exercise.get(setNumber + 1)).get("title"));

                    //animate the change in edittext value
                    final TextView exerciseReps = relativeLayout.findViewById(R.id.exercise_reps);
                    final TextView exerciseWeight = relativeLayout.findViewById(R.id.exercise_weight);
                    final ArrayList finalExercise = exercise;
                    final int finalSetNumber = setNumber;
                    final Animation in = new AlphaAnimation(0.0f, 1.0f);
                    in.setDuration(75);
                    final Animation out = new AlphaAnimation(1.0f, 0.0f);
                    out.setDuration(75);

                    exerciseReps.setText("");
                    exerciseReps.startAnimation(out);
                    exerciseWeight.setText("");
                    exerciseWeight.startAnimation(out);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable()
                    {
                        @Override public void run()
                        {
                            exerciseReps.setText(Integer.toString((Integer) ((HashMap) finalExercise.get(finalSetNumber + 1)).get("reps")));
                            exerciseReps.startAnimation(in);
                            exerciseWeight.setText(Double.toString((Double) ((HashMap) finalExercise.get(finalSetNumber + 1)).get("weight")));
                            exerciseWeight.startAnimation(in);
                        }
                    }, 75);

                    TextView exerciseSets = relativeLayout.findViewById(R.id.set_number);
                    exerciseSets.setText("Sets Completed: "+(setNumber)+" of "+(exercise.size()-1));
                }
            }

        }
    }
    public class onIncrementSet implements View.OnClickListener { //set back button
        @Override
        public void onClick(View v) {
            LinearLayout linearLayout = (LinearLayout)((View)((View)v.getParent()).getParent()).getParent();
            int exerciseNumber = 0;
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                if(linearLayout.getChildAt(i) == ((View)v.getParent()).getParent()){
                    exerciseNumber = i;
                    break;
                }
            }



            ArrayList exercise = exercises.get(exerciseNumber);
            int setNumber = (int) exercise.get(0);
            RelativeLayout relativeLayout = (RelativeLayout) v.getParent();
            if(setNumber > exercise.size()-2){
                return;
            }else if(setNumber == exercise.size()-2) { //done button to finish last set
                //TODO add setting that disables this vibration
                Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(100);
                exercise.set(0, setNumber+1); //increment the set number by 1
                LinearLayout checkboxLinearLayout = ((View) v.getParent()).findViewById(R.id.checkbox_linear_layout);
                for(int i=0; i<=setNumber; i++) {
                    checkboxLinearLayout.getChildAt(i);
                    checkboxLinearLayout.removeViewAt(i);

                    ImageView checkbox = new ImageView(context);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            checkmark_size,
                            checkmark_size
                    );
                    if(i != 0) {
                        params.setMarginStart(10);
                    }
                    checkbox.setLayoutParams(params);
                    checkbox.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
                    checkboxLinearLayout.addView(checkbox, i);
                    TextView exerciseSets = relativeLayout.findViewById(R.id.set_number);
                    //this text will get set twice, once here and once in the colapse card since the colapse card does not have the set number param
                    //the settext in the collpase card is meant only to add a newline
                    exerciseSets.setText("Sets Completed: "+(setNumber+1)+" of "+(exercise.size()-1));
                }

                collapseCard(relativeLayout);
                if(exerciseNumber+1 < linearLayout.getChildCount()) { //this expands the next view
                    RelativeLayout relativeLayoutNext = (RelativeLayout) linearLayout.getChildAt(exerciseNumber + 1);
                    expandCard(relativeLayoutNext);
                }


            } else{ //increment set by 1
                //TODO add setting that disables this vibration
                Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(100);
                exercise.set(0, setNumber+1); //increment the set number by 1
                LinearLayout checkboxLinearLayout = ((View) v.getParent()).findViewById(R.id.checkbox_linear_layout);
                for(int i=0; i<=setNumber; i++) {
                    checkboxLinearLayout.getChildAt(i);
                    checkboxLinearLayout.removeViewAt(i);

                    ImageView checkbox = new ImageView(context);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            checkmark_size,
                            checkmark_size
                    );
                    if(i != 0) {
                        params.setMarginStart(10);
                    }
                    checkbox.setLayoutParams(params);
                    checkbox.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
                    checkboxLinearLayout.addView(checkbox, i);
                }

                TextView exerciseName = relativeLayout.findViewById(R.id.exercise_name);
                exerciseName.setText((String) ((HashMap) exercise.get(setNumber + 2)).get("title"));

                //animate the change in edittext value
                final TextView exerciseReps = relativeLayout.findViewById(R.id.exercise_reps);
                final TextView exerciseWeight = relativeLayout.findViewById(R.id.exercise_weight);
                final ArrayList finalExercise = exercise;
                final int finalSetNumber = setNumber;
                final Animation in = new AlphaAnimation(0.0f, 1.0f);
                in.setDuration(75);
                final Animation out = new AlphaAnimation(1.0f, 0.0f);
                out.setDuration(75);

                exerciseReps.setText("");
                exerciseReps.startAnimation(out);
                exerciseWeight.setText("");
                exerciseWeight.startAnimation(out);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable()
                {
                    @Override public void run()
                    {
                        exerciseReps.setText(Integer.toString((Integer) ((HashMap) finalExercise.get(finalSetNumber + 2)).get("reps")));
                        exerciseReps.startAnimation(in);
                        exerciseWeight.setText(Double.toString((Double) ((HashMap) finalExercise.get(finalSetNumber + 2)).get("weight")));
                        exerciseWeight.startAnimation(in);
                    }
                }, 75);

                TextView exerciseSets = relativeLayout.findViewById(R.id.set_number);
                exerciseSets.setText("Sets Completed: "+(setNumber+1)+" of "+(exercise.size()-1));
            }


        }

    }

    private void expandCard(RelativeLayout relativeLayout){
        View dividerLineNext = relativeLayout.findViewById(R.id.divider_line);
        dividerLineNext.setVisibility(View.VISIBLE);
        ImageButton weightAddButtonNext = relativeLayout.findViewById(R.id.weight_add_button);
        weightAddButtonNext.setVisibility(View.VISIBLE);
        ImageButton weightSubtractButtonNext = relativeLayout.findViewById(R.id.weight_subtract_button);
        weightSubtractButtonNext.setVisibility(View.VISIBLE);
        ImageButton repsAddButtonNext = relativeLayout.findViewById(R.id.reps_add_button);
        repsAddButtonNext.setVisibility(View.VISIBLE);
        ImageButton repsSubtractButtonNext = relativeLayout.findViewById(R.id.reps_subtract_button);
        repsSubtractButtonNext.setVisibility(View.VISIBLE);
        Button setBackButtonNext = relativeLayout.findViewById(R.id.exercise_sets_back_button);
        setBackButtonNext.setVisibility(View.VISIBLE);
        Button doneButtonNext = relativeLayout.findViewById(R.id.done_button);
        doneButtonNext.setVisibility(View.VISIBLE);
        RelativeLayout exerciseSetsRLNext = relativeLayout.findViewById(R.id.exercise_sets_RL);
        RelativeLayout.LayoutParams paramsNext = (RelativeLayout.LayoutParams) exerciseSetsRLNext.getLayoutParams();
        paramsNext.addRule(RelativeLayout.BELOW, R.id.weight_relative_layout);
        paramsNext.setMargins(0, 20, 0, 0);
        exerciseSetsRLNext.setLayoutParams(paramsNext);
        TextView exerciseSets = relativeLayout.findViewById(R.id.set_number);
        exerciseSets.setText(exerciseSets.getText().toString().replace("Sets Completed: \n", "Sets Completed: "));
        TextView exerciseReps = relativeLayout.findViewById(R.id.exercise_reps);
        TextView exerciseWeight = relativeLayout.findViewById(R.id.exercise_weight);
        exerciseReps.setEnabled(true);
        exerciseWeight.setEnabled(true);
        currentlyExpandedCard = relativeLayout;
    }
    private void collapseCard(RelativeLayout relativeLayout){
        View dividerLine = relativeLayout.findViewById(R.id.divider_line);
        dividerLine.setVisibility(View.GONE);
        ImageButton weightAddButton = relativeLayout.findViewById(R.id.weight_add_button);
        weightAddButton.setVisibility(View.GONE);
        ImageButton weightSubtractButton = relativeLayout.findViewById(R.id.weight_subtract_button);
        weightSubtractButton.setVisibility(View.GONE);
        ImageButton repsAddButton = relativeLayout.findViewById(R.id.reps_add_button);
        repsAddButton.setVisibility(View.GONE);
        ImageButton repsSubtractButton = relativeLayout.findViewById(R.id.reps_subtract_button);
        repsSubtractButton.setVisibility(View.GONE);
        Button setBackButton = relativeLayout.findViewById(R.id.exercise_sets_back_button);
        setBackButton.setVisibility(View.GONE);
        Button doneButton = relativeLayout.findViewById(R.id.done_button);
        doneButton.setVisibility(View.GONE);
        RelativeLayout exerciseSetsRL = relativeLayout.findViewById(R.id.exercise_sets_RL);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) exerciseSetsRL.getLayoutParams();
        params.addRule(RelativeLayout.BELOW, R.id.exercise_name);
        params.setMargins(0, 10, 0, 0);
        exerciseSetsRL.setLayoutParams(params);
        TextView exerciseSets = relativeLayout.findViewById(R.id.set_number);
        exerciseSets.setText(exerciseSets.getText().toString().replace("Sets Completed: ", "Sets Completed: \n"));
        TextView exerciseReps = relativeLayout.findViewById(R.id.exercise_reps);
        TextView exerciseWeight = relativeLayout.findViewById(R.id.exercise_weight);
        exerciseReps.setEnabled(false);
        exerciseWeight.setEnabled(false);


    }
    public class onAddOrSubtractClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            RelativeLayout relativeLayout = (RelativeLayout)v.getParent();
            LinearLayout linearLayout = (LinearLayout)((View)((View)v.getParent()).getParent()).getParent().getParent();
            int exerciseNumber = 0;
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                if(linearLayout.getChildAt(i) == ((View)v.getParent()).getParent().getParent()){
                    exerciseNumber = i;
                    break;
                }
            }
            int setNumber = (int)exercises.get(exerciseNumber).get(0)+1;
            if(setNumber > exercises.get(exerciseNumber).size()-1){
                setNumber = exercises.get(exerciseNumber).size()-1;
            }
            if(v.getId() == R.id.weight_add_button) {
                EditText editText = relativeLayout.findViewById(R.id.exercise_weight);
                Double currentWeight = Double.parseDouble(editText.getText().toString()) + 2.5;
                String currentWeightString = String.valueOf(currentWeight);
                editText.setText(currentWeightString);
                ((HashMap)exercises.get(exerciseNumber).get(setNumber)).put("weight", currentWeight);
            }
            if(v.getId() == R.id.weight_subtract_button) {
                EditText editText = relativeLayout.findViewById(R.id.exercise_weight);
                Double currentWeight = Double.parseDouble(editText.getText().toString()) - 2.5;
                String currentWeightString = String.valueOf(currentWeight);
                editText.setText(currentWeightString);
                ((HashMap)exercises.get(exerciseNumber).get(setNumber)).put("weight", currentWeight);


            }
            if(v.getId() == R.id.reps_add_button) {
                EditText editText = relativeLayout.findViewById(R.id.exercise_reps);
                Integer currentReps = Integer.parseInt(editText.getText().toString()) + 1;
                String currentRepsString = String.valueOf(currentReps);
                editText.setText(currentRepsString);
                ((HashMap)exercises.get(exerciseNumber).get(setNumber)).put("reps", currentReps);

            }
            if(v.getId() == R.id.reps_subtract_button) {
                EditText editText = relativeLayout.findViewById(R.id.exercise_reps);
                Integer currentReps = Integer.parseInt(editText.getText().toString()) - 1;
                String currentRepsString = String.valueOf(currentReps);
                editText.setText(currentRepsString);
                ((HashMap)exercises.get(exerciseNumber).get(setNumber)).put("reps", currentReps);

            }
            System.out.println(exercises);

        }
    }

    public class onExpandClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            LinearLayout linearLayout = (LinearLayout)v.getParent();
            int exerciseNumber = 0;
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                if(linearLayout.getChildAt(i) == v){
                    exerciseNumber = i;
                    break;
                }
            }
            if((int)exercises.get(exerciseNumber).get(0) == exercises.get(exerciseNumber).size()-1){
                v.findViewById(R.id.exercise_sets_back_button).performClick();
            }
            System.out.println(exercises);
            //expand the card that was clicked
            collapseCard(currentlyExpandedCard);
            expandCard((RelativeLayout)v);
        }
    }


    private class onOverflowClick implements View.OnClickListener {
        private Context mContext;

        private onOverflowClick(Context context){
            mContext = context;
        }
        @Override
        public void onClick(View v) {
            // This is an android.support.v7.widget.PopupMenu;
            PopupMenu popupMenu = new PopupMenu(context, v);
            popupMenu.inflate(R.menu.workout_overflow_menu);
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
                 @Override
                 public boolean onMenuItemClick(MenuItem item) {
                     switch (item.getItemId()) {
                         case R.id.workout_overflow_info:
                             System.out.println("INFO");
                             return true;

                         case R.id.workout_overflow_replace:
                             System.out.println("REPLACE");
                             return true;

                         case R.id.workout_overflow_delete:
                             System.out.println("DELETE");
                             return true;

                         default:
                             return true;
                     }
                 }
             }
            );
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.finish_menu, menu);
        return true;
    }


}