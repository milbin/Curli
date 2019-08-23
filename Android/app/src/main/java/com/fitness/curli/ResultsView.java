package com.fitness.curli;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
        String[] resultsList = new String[]{"Weight", "Blood pressure", "Muscle Mass"};
        LinearLayout resultLinearLayout = findViewById(R.id.resultsList);

        for (String result : resultsList){
            View card = LayoutInflater.from(context).inflate(R.layout.result_card, null);
            TextView title = card.findViewById(R.id.title);
            title.setText(result);
            resultLinearLayout.addView(card);
        }
    }
}
