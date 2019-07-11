package com.fitness.curli;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WorkoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_activity);
        LinearLayout linearLayout = findViewById(R.id.linearLayoutWorkout);

        View relativeLayout = LayoutInflater.from(this).inflate(R.layout.exercise_card, null);
        linearLayout.addView(relativeLayout);
        TextView exerciseName = relativeLayout.findViewById(R.id.exercise_name);
        exerciseName.setText("Leg Press");
        relativeLayout.setOnClickListener(new onExerciseClick());



        View relativeLayout1 = LayoutInflater.from(this).inflate(R.layout.exercise_card, null);
        linearLayout.addView(relativeLayout1);
        TextView exerciseName1 = relativeLayout1.findViewById(R.id.exercise_name);
        exerciseName1.setText("Leg Press");


    }

    public class onExerciseClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            LinearLayout linearLayout = v.findViewById(R.id.checkbox_linear_layout);
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