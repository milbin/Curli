package com.fitness.curli;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Dimension;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

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
        ArrayList<String> groups = SQLData.getGroups();

        int groupNumber = groups.size();
        int groupNameIndex = 0;
        int cardsPerRow = 2;
        boolean adding = true;

        LinearLayout linearLayout = findViewById(R.id.MuscleViewLinearLayout);

        Display display = getWindowManager().getDefaultDisplay();

        int layoutWidth = display.getWidth();

        System.out.println(layoutWidth);
        while (adding){
            LinearLayout rowLayout = new LinearLayout(this);
            for (int x = 0; x < cardsPerRow; x++){
                View card = LayoutInflater.from(context).inflate(R.layout.exercise_card_info_view, null);
                RelativeLayout rel = card.findViewById(R.id.group);

                ViewGroup.LayoutParams params = rel.getLayoutParams();
                params.width = layoutWidth/cardsPerRow;
                rel.setLayoutParams(params);

                TextView title = card.findViewById(R.id.title);
                title.setText(groups.get(groupNameIndex));

                card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MuscleView.this, InfoView.class);
                        startActivity(intent);
                    }
                });

                rowLayout.addView(card);

                if (groupNameIndex == groupNumber-1){
                    adding = false;
                    break;
                }
                groupNameIndex++;
            }
            linearLayout.addView(rowLayout);
        }

        dialog.dismiss();

    }
}
