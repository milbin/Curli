package com.fitness.curli;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
    //{"name": "Arms and Chest", "exercises": [[0, {"title": "Bench Press", "weight":135, "reps":8},
    //{"title": "Bench Press", "weight":135, "reps":8}, {"title": "Bench Press", "weight":135, "reps":8}],
    //[0, {"title": "Bicep Curl", "weight":75, "reps":8}, {"title": "Bicep Curl", "weight":75, "reps":8},
    //{"title": "Bicep Curl", "weight":75, "reps":8}]]}

    ArrayList<ArrayList> exercises;
    Context context;
    int checkmark_size = 70;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_activity);
        LinearLayout linearLayout = findViewById(R.id.linearLayoutWorkout);
        context = this;

        HashMap workout = (HashMap) getIntent().getSerializableExtra("workout");
        setTitle((String)workout.get("title"));


        exercises = (ArrayList<ArrayList>) workout.get("exercises");
        for(ArrayList<HashMap> exercise:exercises) {
            View relativeLayout = LayoutInflater.from(this).inflate(R.layout.exercise_card, null);
            linearLayout.addView(relativeLayout);

            ImageButton overflowButton = relativeLayout.findViewById(R.id.overflowButton);
            overflowButton.setOnClickListener(new onOverflowClick(context));
            Button doneButton = relativeLayout.findViewById(R.id.done_button);
            doneButton.setOnClickListener(new onExerciseClick());

            TextView exerciseName = relativeLayout.findViewById(R.id.exercise_name);
            EditText exerciseReps = relativeLayout.findViewById(R.id.exercise_reps);
            EditText exerciseWeight = relativeLayout.findViewById(R.id.exercise_weight);
            TextView excerciseSets = relativeLayout.findViewById(R.id.set_number);


            exerciseName.setText((String) exercise.get(1).get("title"));
            exerciseReps.setText(Integer.toString((Integer) exercise.get(1).get("reps")));
            exerciseWeight.setText(Integer.toString((Integer) exercise.get(1).get("weight")));
            excerciseSets.setText("Sets: " + 0 + "/" + (exercise.size() - 1));

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
                    checkbox.setLayoutParams(params);
                }
                exerciseSets.addView(checkbox);
            }
        }






    }


    public class onExerciseClick implements View.OnClickListener {

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
            exercise.set(0, setNumber+1); //increment the set number by 1
            RelativeLayout relativeLayout = (RelativeLayout) v.getParent();

            if(setNumber > exercise.size()-2){
                return;
            }else if(setNumber == exercise.size()-2){
                LinearLayout checkboxLinearLayout = ((View)v.getParent()).findViewById(R.id.checkbox_linear_layout);
                checkboxLinearLayout.getChildAt(setNumber);
                checkboxLinearLayout.removeViewAt(setNumber);
                ImageView checkbox = new ImageView(context);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        checkmark_size,
                        checkmark_size
                );
                params.setMarginStart(10);
                checkbox.setLayoutParams(params);
                checkbox.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
                checkboxLinearLayout.addView(checkbox, setNumber);

                TextView exerciseSets = relativeLayout.findViewById(R.id.set_number);
                exerciseSets.setText("Sets: "+(setNumber+1)+"/"+(setNumber+1));
            }else {
                LinearLayout checkboxLinearLayout = ((View) v.getParent()).findViewById(R.id.checkbox_linear_layout);
                checkboxLinearLayout.getChildAt(setNumber);
                checkboxLinearLayout.removeViewAt(setNumber);
                ImageView checkbox = new ImageView(context);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        checkmark_size,
                        checkmark_size
                );
                if(setNumber != 0) {
                    params.setMarginStart(10);
                }
                checkbox.setLayoutParams(params);
                checkbox.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
                checkboxLinearLayout.addView(checkbox, setNumber);

                TextView exerciseName = relativeLayout.findViewById(R.id.exercise_name);
                exerciseName.setText((String) ((HashMap) exercise.get(setNumber + 1)).get("title"));

                TextView exerciseReps = relativeLayout.findViewById(R.id.exercise_reps);
                exerciseReps.setText(Integer.toString((Integer) ((HashMap) exercise.get(setNumber + 2)).get("reps")));

                TextView exerciseWeight = relativeLayout.findViewById(R.id.exercise_weight);
                exerciseWeight.setText(Integer.toString((Integer) ((HashMap) exercise.get(setNumber + 2)).get("weight")));

                TextView exerciseSets = relativeLayout.findViewById(R.id.set_number);
                exerciseSets.setText("Sets: "+(setNumber+1)+"/"+(exercise.size()-1));
            }
        }
    }

    public class onOverflowClick implements View.OnClickListener {
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
}