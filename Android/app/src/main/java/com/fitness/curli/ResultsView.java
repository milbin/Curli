package com.fitness.curli;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.text.PrecomputedTextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ResultsView extends AppCompatActivity {
    ProgressDialog dialog;
    Context context;
    boolean fabClick = false;
    LinearLayout fabList;

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
        fabList = findViewById(R.id.fab_list);
        for (String resultName : resultsList){
            RelativeLayout card = new RelativeLayout(this);
            card.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            card.setBackgroundColor(getResources().getColor(R.color.white));
            TextView title = new TextView(this);
            title.setText(resultName);
            title.setGravity(Gravity.CENTER_HORIZONTAL);
            card.setGravity(Gravity.CENTER_HORIZONTAL);
            card.addView(title);
            fabList.addView(card);
        }
        fabList.setVisibility(View.GONE);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fabClick) {
                    fabList.setVisibility(View.GONE);
                    fabClick = false;
                }
                else if (!fabClick){
                    fabList.setVisibility(View.VISIBLE);
                    fabClick = true;
                }
            }
        });
        /*
        String[] resultsList = new String[]{"Weight", "Blood pressure", "Muscle Mass"};
        LinearLayout resultLinearLayout = findViewById(R.id.resultsList);

        for (String result : resultsList){
            View card = LayoutInflater.from(context).inflate(R.layout.result_card, null);
            TextView title = card.findViewById(R.id.title);
            title.setText(result);
            resultLinearLayout.addView(card);
        }
        */
    }
}
