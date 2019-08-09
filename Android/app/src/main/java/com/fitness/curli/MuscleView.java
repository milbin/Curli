package com.fitness.curli;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MuscleView extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muscle_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("");

        setSupportActionBar(toolbar);

    }
}
