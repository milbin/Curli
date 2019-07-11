package com.fitness.curli;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

public class ExerciseView extends AppCompatActivity {
    ProgressDialog dialog;
    Context context;
    ArrayList<String> exercises;
    LinearLayout linearLayout;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_view);

        dialog = ProgressDialog.show(ExerciseView.this, "", "Loading...", true);

        context = getApplicationContext();

        linearLayout = findViewById(R.id.ExerciseViewLinearLayout);
        Intent intent = getIntent();
        exercises = intent.getStringArrayListExtra("exercises");

        getExcercises();
    }

    public void getExcercises(){
        int length = exercises.size();

        for (int i = 0; i < length; i++){
            View card = LayoutInflater.from(context).inflate(R.layout.single_group, null);
            TextView title = card.findViewById(R.id.title);
            title.setText(exercises.get(i));

            linearLayout.addView(card);
        }

        dialog.dismiss();
    }
}
