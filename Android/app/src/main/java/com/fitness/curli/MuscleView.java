package com.fitness.curli;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

public class MuscleView extends AppCompatActivity {

    Toolbar toolbar;
    ProgressDialog dialog;
    Context context;
    ExerciseDb SQLData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muscle_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("");

        setSupportActionBar(toolbar);

        dialog = ProgressDialog.show(MuscleView.this, "", "Loading...", true);

        context = getApplicationContext();
        SQLData = new ExerciseDb(context);
        SQLData.open();

        displayMuscles();

    }

    public void displayMuscles(){
        ArrayList groups = SQLData.getGroups();
        System.out.println(groups);

        dialog.dismiss();

    }
}
