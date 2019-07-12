package com.fitness.curli;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
    //{"name": "Arms and Chest", "exercises": [[{"title": "Bench Press", "weight":135, "reps":8},
    //{"title": "Bench Press", "weight":135, "reps":8}, {"title": "Bench Press", "weight":135, "reps":8}],
    //[{"title": "Bicep Curl", "weight":75, "reps":8}, {"title": "Bench Press", "weight":75, "reps":8},
    // {"title": "Bench Press", "weight":75, "reps":8}]]}

    ArrayList<ArrayList> exercises;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_activity);
        LinearLayout linearLayout = findViewById(R.id.linearLayoutWorkout);

        HashMap workout = (HashMap) getIntent().getSerializableExtra("workout");
        setTitle((String)workout.get("name"));

        exercises = (ArrayList<ArrayList>) workout.get("exercises");
        for(ArrayList<HashMap> exercise:exercises){
            View relativeLayout = LayoutInflater.from(this).inflate(R.layout.exercise_card, null);
            linearLayout.addView(relativeLayout);

            TextView exerciseName = relativeLayout.findViewById(R.id.exercise_name);
            exerciseName.setText((String)exercise.get(0).get("title"));

            TextView exerciseReps = relativeLayout.findViewById(R.id.exercise_reps);
            exerciseReps.setText(Integer.toString((Integer)exercise.get(0).get("reps")));

            TextView exerciseWeight = relativeLayout.findViewById(R.id.exercise_weight);
            exerciseWeight.setText(Integer.toString((Integer)exercise.get(0).get("weight")));

            LinearLayout exerciseSets = relativeLayout.findViewById(R.id.checkbox_linear_layout);
            for(HashMap i:exercise){
                ImageView checkbox = new ImageView(this);
                checkbox.setImageDrawable(getDrawable(R.drawable.ic_check_circle_grey_24dp));
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMarginStart(10);
                checkbox.setLayoutParams(params);
                exerciseSets.addView(checkbox);
            }

            Button doneButton = relativeLayout.findViewById(R.id.done_button);
            doneButton.setOnClickListener(new onExerciseClick());
        }



    }

    public class onExerciseClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            LinearLayout linearLayout = ((View)v.getParent()).findViewById(R.id.checkbox_linear_layout);
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                ImageView checkbox = (ImageView) linearLayout.getChildAt(i);
                System.out.println(checkbox.getDrawable());
                System.out.println(getDrawable(R.drawable.ic_check_circle_grey_24dp));
                if(areDrawablesIdentical(checkbox.getDrawable(),getDrawable(R.drawable.ic_check_circle_grey_24dp))){
                    checkbox.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
                    break;
                }
            }
        }
    }


    public static boolean areDrawablesIdentical(Drawable drawableA, Drawable drawableB) {
        Drawable.ConstantState stateA = drawableA.getConstantState();
        Drawable.ConstantState stateB = drawableB.getConstantState();
        // If the constant state is identical, they are using the same drawable resource.
        // However, the opposite is not necessarily true.
        return (stateA != null && stateB != null && stateA.equals(stateB))
                || getBitmap(drawableA).sameAs(getBitmap(drawableB));
    }

    public static Bitmap getBitmap(Drawable drawable) {
        Bitmap result;
        if (drawable instanceof BitmapDrawable) {
            result = ((BitmapDrawable) drawable).getBitmap();
        } else {
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            // Some drawables have no intrinsic width - e.g. solid colours.
            if (width <= 0) {
                width = 1;
            }
            if (height <= 0) {
                height = 1;
            }

            result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }
        return result;
    }
}