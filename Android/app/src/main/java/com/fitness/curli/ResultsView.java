package com.fitness.curli;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ResultsView extends AppCompatActivity {
    ProgressDialog dialog;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_view);

        dialog = ProgressDialog.show(this, "Loading...", "", false);

        context = getApplicationContext();

        displayResults();

        dialog.dismiss();
    }

    public void displayResults(){

    }
}
